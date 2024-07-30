package gift.controller;

import gift.auth.AuthService;
import gift.auth.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@Tag(name = "OAuth", description = "OAuth 인증 관련 API")
public class KakaoOAuthController {

    private final AuthService kakaoAuthService;

    public KakaoOAuthController(@Qualifier("kakaoAuthService") AuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인 페이지로 리다이렉트", description = "카카오 로그인 페이지로 리다이렉트합니다.")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = kakaoAuthService.getLoginUrl();
        response.sendRedirect(kakaoLoginUrl);
    }

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백 처리", description = "카카오 로그인 콜백을 처리하고 액세스 토큰을 반환합니다.")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        Map<String, String> tokens = ((KakaoAuthService) kakaoAuthService).handleCallback(code);
        return ResponseEntity.ok(tokens);
    }
}
