package gift.Controller;

import gift.Model.DTO.MemberDTO;
import gift.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "로그인 API", description = "로그인과 관련된 API")
@RestController
@RequestMapping("/api/members")
public class LoginRestController {
    private final UserService userService;

    public LoginRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok(userService.register(memberDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok(userService.login(memberDTO));
    }

    @GetMapping("/kakao/link")
    public void kakaoLink(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", userService.getKakaoLoginUrl());
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        ResponseEntity<?> result = userService.kakaoLogin(code);
        return result;
    }
}
