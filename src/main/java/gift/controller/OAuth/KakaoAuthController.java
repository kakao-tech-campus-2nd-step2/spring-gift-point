package gift.controller.OAuth;

import gift.dto.OAuth.LoginInfoResponse;
import gift.service.OAuth.KakaoAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    public ResponseEntity<Void> getAuthCode(HttpServletResponse response,
                                            @RequestParam("redirect-url") String redirectUrl) throws IOException {
        String url = kakaoAuthService.createCodeUrl(redirectUrl);

        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("location", url)
                .build();
    }

    @GetMapping("/login/callback")
    public ResponseEntity<Map<String, String>> getAccessToken(
            @RequestParam String code,
            @RequestParam("redirect-url") String redirectUrl) {
        LoginInfoResponse.Info loginInfo = kakaoAuthService.register(code, redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", loginInfo.token());

        Map<String, String> responseBody = Map.of("name", loginInfo.name());
        return ResponseEntity.ok().headers(headers).body(responseBody);
    }
}