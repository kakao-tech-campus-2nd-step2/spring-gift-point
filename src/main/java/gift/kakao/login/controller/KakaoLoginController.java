package gift.kakao.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API")
public class KakaoLoginController {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/kakao")
    public void showKakaoLogin(HttpServletResponse response) throws IOException {
        String uri = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(uri);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인")
    public void handleKakaoLoginPost(HttpServletResponse response) throws IOException {
        String redirectUri = "/api/members/kakao";
        response.sendRedirect(redirectUri);
    }
}
