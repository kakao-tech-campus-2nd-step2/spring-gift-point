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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@Tag(name = "Kakao Authentication System", description = "Operations related to Kakao authentication")
public class KakaoController {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String GRANT_TYPE = "authorization_code";

    private final KakaoService kakaoService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-base-url}")
    private String authBaseUrl;

    @Value("${kakao.scope}")
    private String scope;

    @Autowired
    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    @Operation(summary = "Redirect to Kakao login", description = "Redirects the user to Kakao login page", tags = { "Kakao Authentication System" })
    public void redirectKakaoLogin(HttpServletResponse response) throws IOException {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8.toString());
        StringBuilder kakaoLoginUrl = new StringBuilder(authBaseUrl)
                .append("?response_type=code")
                .append("&client_id=").append(clientId)
                .append("&redirect_uri=").append(encodedRedirectUri)
                .append("&scope=").append(encodedScope);

        response.sendRedirect(kakaoLoginUrl.toString());
    }

    @GetMapping("/callback")
    @Operation(summary = "Kakao login callback", description = "Handles the callback after Kakao login", tags = { "Kakao Authentication System" })
    public ResponseEntity<String> callback(
            @Parameter(description = "Authorization code from Kakao", required = false)
            @RequestParam(required = false) String code) {
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }
        String accessToken = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/member")
    @Operation(summary = "Get Kakao member information", description = "Fetches the Kakao member information using the authorization token", tags = { "Kakao Authentication System" })
    public ResponseEntity<String> getMember(
            @Parameter(description = "Authorization token", required = true)
            @RequestHeader(value = AUTHORIZATION_HEADER) String authorizationHeader) {
        String accessToken = authorizationHeader.replace(BEARER_PREFIX, "");
        return ResponseEntity.ok(kakaoService.getMember(accessToken).toString());
    }
}
