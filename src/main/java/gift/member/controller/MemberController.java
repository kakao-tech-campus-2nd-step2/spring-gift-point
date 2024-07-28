package gift.member.controller;

import gift.common.util.CommonResponse;
import gift.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        memberService.registerMember(email, password);
        String token = memberService.login(email, password);
        return ResponseEntity.ok(new CommonResponse<>(token, "회원가입 성공", true));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String token = memberService.login(email, password);

        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        return ResponseEntity.ok(new CommonResponse<>(token, "로그인 성공 후 토큰 발급 성공", true));
    }
}
