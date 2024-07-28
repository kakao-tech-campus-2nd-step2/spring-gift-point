package gift.member.controller;

import gift.common.util.CommonResponse;
import gift.member.dto.LoginRequest;
import gift.member.dto.RegisterRequest;
import gift.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member API", description = "회원가입, 로그인 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        memberService.registerMember(email, password);
        String token = memberService.login(email, password);
        return ResponseEntity.ok(new CommonResponse<>(token, "회원가입 성공", true));
    }

    // 로그인
    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String token = memberService.login(email, password);

        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        return ResponseEntity.ok(new CommonResponse<>(token, "로그인 성공 후 토큰 발급 성공", true));
    }
}
