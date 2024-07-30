package gift.controller.auth;

import gift.config.properties.KakaoProperties;
import gift.controller.api.AuthApi;
import gift.dto.auth.AuthResponse;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import gift.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;

    public AuthController(AuthService authService, KakaoProperties kakaoProperties) {
        this.authService = authService;
        this.kakaoProperties = kakaoProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        var auth = authService.register(registerRequest);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var auth = authService.login(loginRequest);
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> redirectKakaoLogin() {
        var headers = getRedirectHeader(kakaoProperties.redirectUri());
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<AuthResponse> loginWithKakaoAuth(@RequestParam String code) {
        var auth = authService.loginWithKakaoAuth(code);
        return ResponseEntity.ok(auth);
    }

    private HttpHeaders getRedirectHeader(String redirectUri) {
        var headers = new HttpHeaders();
        String redirectLocation = kakaoProperties.oauthBaseUri() + "&client_id=" + kakaoProperties.restApiKey() + "&redirect_uri=" + redirectUri;
        headers.setLocation(URI.create(redirectLocation));
        return headers;
    }
}
