package gift.controller.OAuth;

import gift.service.OAuth.KakaoAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/kakao")
public class KakaoAuthController implements KakaoAuthSpecification {

    private final KakaoAuthService kakaoAuthService;

    @Autowired
    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login")
    public void getAuthCode(HttpServletResponse response) throws IOException {
        String url = kakaoAuthService.createCodeUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestParam String code) {
        String token = kakaoAuthService.register(code);
        return ResponseEntity.ok(Map.of("access_token", token));
    }
}