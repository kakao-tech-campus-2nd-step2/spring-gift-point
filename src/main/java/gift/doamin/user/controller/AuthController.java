package gift.doamin.user.controller;

import gift.doamin.user.dto.LoginForm;
import gift.doamin.user.dto.SignUpForm;
import gift.doamin.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "로그인, 회원가입, 토큰 갱신 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "회원가입", description = "이메일과 비밀번호를 입력하여 새로운 회원으로 등록합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody SignUpForm signUpForm) {
        authService.signUp(signUpForm);
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginForm loginForm) {
        String token = authService.login(loginForm);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, token)
            .build();
    }

    @Operation(summary = "접근 토큰 갱신", description = "접근 토큰을 갱신합니다. Authorization")
    @GetMapping("/accessToken")
    public ResponseEntity<Void> refreshToken(@CookieValue String refreshToken) {
        String accessToken = authService.makeNewAccessToken(refreshToken);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .build();
    }
}
