package gift.controller;

import gift.dto.response.LoginResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members/v2")
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberRestController {

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 처리합니다.")
    public LoginResponse login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        String token = memberService.login(email, password);
        session.setAttribute("user", email);
        if ("admin@kakao.com".equals(email) && "admin".equals(password)) {
            return new LoginResponse(token, "ADMIN");
        } else {
            return new LoginResponse(token, "USER");
        }
    }

}