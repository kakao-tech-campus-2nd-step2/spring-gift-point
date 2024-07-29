package gift.controller;

import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class KakaoController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String GRANT_TYPE = "authorization_code";

    private final KakaoService kakaoService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public void redirectKakaoLogin(HttpServletResponse response) throws IOException {
        String scope = "profile_nickname,profile_image";
        String kakaoLoginUrl = String.format(
                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                clientId, redirectUri, scope
        );
        response.sendRedirect(kakaoLoginUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }
        String accessToken = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/member")
    public ResponseEntity<String> getMember(@RequestHeader(value = AUTHORIZATION_HEADER) String authorizationHeader) {
        String accessToken = authorizationHeader.replace(BEARER_PREFIX, "");
        return ResponseEntity.ok(kakaoService.getMember(accessToken).toString());
    }
}
