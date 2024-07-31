package gift.controller;

import gift.service.KakaoTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController("api/kakao")
public class KakaoAuthController {
    /*@Value("${kakao.app.key}")
    private String appKey;

    private final KakaoTokenService kakaoTokenService;

    public KakaoAuthController(KakaoTokenService kakaoTokenService) {
        this.kakaoTokenService = kakaoTokenService;
    }

    @GetMapping("/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String redirectUri = "http://localhost:8080/callback"; // 리디렉션 URI
        String kakaoAuthUrl = String.format("https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=%s&client_id=%s", redirectUri, appKey);
        response.sendRedirect(kakaoAuthUrl); // 카카오 인가 페이지로 리다이렉트
    }

    @GetMapping("/callback")
    public String getKakaoAuthorizationCode(@RequestParam("code") String authorizationCode) {
        String accessToken = kakaoTokenService.getAccessToken(authorizationCode);
        System.out.println("Access Token: " + accessToken);
        return accessToken;
    }*/
}
