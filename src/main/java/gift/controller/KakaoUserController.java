package gift.controller;

import gift.service.KakaoUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "카카오 인가 및 토큰 발급 API", description = "인가 요청 시 토큰이 발급됩니다.")
@RestController
@RequestMapping("/api/kakao")
public class KakaoUserController {
    private KakaoUserService kakaoUserService;

    @Autowired
    public KakaoUserController(KakaoUserService kakaoUserService) {
        this.kakaoUserService = kakaoUserService;
    }

    @Operation(summary = "카카오 인가 코드 발급", description = "카카오 로그인 후 동의 화면을 통해 인가 코드가 발급됩니다.")
    @GetMapping("/authorize")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = kakaoUserService.getAuthorizeUrl();
        response.sendRedirect(kakaoAuthUrl);
    }

    @Operation(summary = "카카오 토큰 발급", description = "인가 코드를 활용해 카카오 인증 토큰이 발급됩니다.")
    @GetMapping("/token")
    public ResponseEntity<String> getTokenGET(@RequestParam String code) {
        String token = kakaoUserService.getAccessToken(code);
        return ResponseEntity.ok(token);
    }
}
