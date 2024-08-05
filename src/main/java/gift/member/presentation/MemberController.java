package gift.member.presentation;

import gift.auth.KakaoService;
import gift.auth.TokenService;
import gift.member.application.MemberService;
import gift.member.application.response.MemberLoginServiceResponse;
import gift.member.application.response.MemberRegisterServiceResponse;
import gift.member.presentation.request.*;
import gift.member.presentation.response.MemberLoginResponse;
import gift.member.presentation.response.MemberRegisterResponse;
import gift.member.presentation.response.PointReadResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController implements MemberApi {
    private final MemberService memberService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, TokenService tokenService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody MemberRegisterRequest request
    ) {
        MemberRegisterServiceResponse member = memberService.register(request.toCommand());
        String token = tokenService.createToken(member.id());


        return ResponseEntity.created(URI.create("/api/members/" + member.id()))
                .body(MemberRegisterResponse.of(member.email(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(
            @RequestBody MemberLoginRequest request
    ) {
        MemberLoginServiceResponse member = memberService.login(request.toCommand());
        String token = tokenService.createToken(member.id());

        return ResponseEntity.ok(MemberLoginResponse.of(member.email(), token));
    }

    @GetMapping("/login/kakao")
    public void kakaoLogin(
            HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(kakaoService.getKakaoRedirectUrl());
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<MemberLoginResponse> kakaoLoginCallback(
            @RequestParam("code") String code
    ) {
        MemberLoginServiceResponse member = memberService.kakaoLogin(code);
        String token = tokenService.createToken(member.id());

        return ResponseEntity.ok(MemberLoginResponse.of(member.email(), token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberControllerResponse> findById(
            @PathVariable("id") Long memberId
    ) {
        return ResponseEntity.ok(MemberControllerResponse.from(memberService.findById(memberId)));
    }

    @GetMapping
    public ResponseEntity<List<MemberControllerResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll().stream().map(MemberControllerResponse::from).toList());
    }

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(
            ResolvedMember resolvedMember,
            @RequestBody MemberEmailUpdateRequest request
    ) {
        memberService.updateEmail(resolvedMember.id(), request.toCommand());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            ResolvedMember resolvedMember,
            @RequestBody MemberPasswordUpdateRequest request
    ) {
        memberService.updatePassword(resolvedMember.id(), request.toCommand());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            ResolvedMember resolvedMember
    ) {
        memberService.delete(resolvedMember.id());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/point")
    public ResponseEntity<PointReadResponse> addPoint(
            ResolvedMember resolvedMember,
            @RequestBody PointUpdateRequest request
    ) {
        memberService.addPoint(resolvedMember.id(), request.amount());
        int updatedPoints = memberService.getPoint(resolvedMember.id());
        return ResponseEntity.ok(new PointReadResponse(updatedPoints));
    }

    @GetMapping("/point")
    public ResponseEntity<PointReadResponse> getPoint(
            ResolvedMember resolvedMember
    ) {
        return ResponseEntity.ok(new PointReadResponse(memberService.getPoint(resolvedMember.id())));
    }
}

