package gift.api.member.controller;

import gift.api.member.dto.MemberRequest;
import gift.api.member.service.KakaoService;
import gift.api.member.service.MemberService;
import gift.global.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<Void> register(@RequestBody @Valid MemberRequest memberRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String accessToken = JwtUtil.generateAccessToken(memberService.register(memberRequest), memberRequest.email(), memberRequest.role());
        responseHeaders.set("Authorization", JwtUtil.generateHeaderValue(accessToken));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "일반 로그인")
    public ResponseEntity<Void> login(@RequestBody MemberRequest memberRequest, @RequestHeader("Authorization") String token) {
        memberService.login(memberRequest, token.split(" ")[1]);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth/kakao")
    @Operation(summary = "카카오 로그인")
    public ResponseEntity<Void> loginKakao(@RequestParam("code") String code) {
        kakaoService.login(code);
        return ResponseEntity.ok().build();
    }
}
