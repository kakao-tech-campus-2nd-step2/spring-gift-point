package gift.controller;

import gift.dto.MemberDto;
import gift.entity.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members")
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberRestController {

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 처리합니다.")
    public Map<String, String> login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = memberService.login(email, password);
            if ("admin@kakao.com".equals(email) && "admin".equals(password)) {
                session.setAttribute("user", email);
                response.put("token", token);
                response.put("role", "ADMIN");
            } else {
                session.setAttribute("user", email);
                response.put("token", token);
                response.put("role", "USER");
            }
            return response;
        } catch (RuntimeException e) {
            response.put("error", "Invalid email or password");
            return response;
        }
    }
}
