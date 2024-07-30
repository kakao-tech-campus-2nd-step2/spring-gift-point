package gift.controller;

import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.service.MemberService;
import gift.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static gift.domain.LoginType.NORMAL;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @Autowired
    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다.")
    public ResponseEntity<Map<String, String>> register(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.register(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.authenticate(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 API로 회원을 인증하고 토큰을 받는다.")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        Map<String, String> response = memberService.handleKakaoLogin(code);
        return ResponseEntity.ok(response);

    }
}
