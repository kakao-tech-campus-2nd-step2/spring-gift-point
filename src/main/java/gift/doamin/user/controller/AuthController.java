package gift.doamin.user.controller;

import gift.doamin.user.dto.LoginRequest;
import gift.doamin.user.dto.SignUpRequest;
import gift.doamin.user.service.AuthService;
import gift.global.util.JwtDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
@RequestMapping("/api/members")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "회원가입", description = "이메일과 비밀번호를 입력하여 새로운 회원으로 등록합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        JwtDto tokens = authService.signUp(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.AUTHORIZATION, tokens.getAccessToken())
            .header("Set-Cookie", makeRefreshTokenCookie(tokens.getRefreshToken()))
            .build();
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtDto tokens = authService.login(loginRequest);

        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, tokens.getAccessToken())
            .header("Set-Cookie", makeRefreshTokenCookie(tokens.getRefreshToken()))
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

    private static String makeRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .maxAge(12 * 60 * 60)
            .path("/api/members/accessToken")
            .build()
            .toString();
    }
}
