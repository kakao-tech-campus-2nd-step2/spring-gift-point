package gift.controller.OAuth;

import gift.dto.OAuth.LoginInfoResponse;
import gift.service.OAuth.KakaoAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth/kakao")
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

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestParam String code) {
        LoginInfoResponse.Info loginInfo = kakaoAuthService.register(code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", loginInfo.token());

        Map<String, String> responseBody = Map.of("name", loginInfo.name());
        return ResponseEntity.ok().headers(headers).body(responseBody);
    }
}