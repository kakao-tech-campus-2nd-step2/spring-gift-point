package gift.controller;

import gift.dto.KakaoTokenResponseDTO;
import gift.dto.KakaoUserDTO;
import gift.model.Member;
import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class KakaoAuthController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login/kakao")
    public void redirectKakaoLogin(HttpServletResponse response) throws IOException {
        String url = kakaoAuthService.getKakaoLoginUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam(name = "code") String code) throws IOException {
        try {
            KakaoTokenResponseDTO tokenResponse = kakaoAuthService.getKakaoToken(code);
            KakaoUserDTO kakaoUserDTO = kakaoAuthService.getKakaoUser(
                tokenResponse.getAccessToken());
            Member member = kakaoAuthService.registerOrGetMember(kakaoUserDTO, tokenResponse.getAccessToken());
            return ResponseEntity.ok(tokenResponse.getAccessToken());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("카카오 로그인 실패: " + e.getMessage());
        }
    }
}
