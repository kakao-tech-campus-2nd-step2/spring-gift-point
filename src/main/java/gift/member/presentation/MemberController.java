package gift.member.presentation;

import gift.auth.KakaoService;
import gift.auth.TokenService;
import gift.member.application.MemberService;
import gift.member.presentation.request.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/member")
public class MemberController implements MemberApi {
    private final MemberService memberService;
    private final TokenService tokenService;
    private final KakaoService kakaoService;

    private static final String AUTHENTICATION_TYPE = "Bearer ";

    public MemberController(MemberService memberService, TokenService tokenService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(
            @RequestBody MemberJoinRequest request
    ) {
        Long memberId = memberService.join(request.toCommand());
        String token = tokenService.createToken(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, AUTHENTICATION_TYPE + token);

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody MemberLoginRequest request
    ) {
        Long memberId = memberService.login(request.toCommand());
        String token = tokenService.createToken(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, AUTHENTICATION_TYPE + token);

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/login/kakao")
    public void kakaoLogin(
            HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(kakaoService.getKakaoRedirectUrl());
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<?> kakaoLoginCallback(
            @RequestParam("code") String code
    ) {
        Long memberId = memberService.kakaoLogin(code);
        String token = tokenService.createToken(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, AUTHENTICATION_TYPE + token);

        return ResponseEntity.ok().headers(headers).build();
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
            @RequestBody MemberEmailUpdateRequest request,
            ResolvedMember resolvedMember
    ) {
        memberService.updateEmail(request.toCommand(), resolvedMember.id());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody MemberPasswordUpdateRequest request,
            ResolvedMember resolvedMember
    ) {
        memberService.updatePassword(request.toCommand(), resolvedMember.id());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            ResolvedMember resolvedMember
    ) {
        memberService.delete(resolvedMember.id());
        return ResponseEntity.noContent().build();
    }
}

