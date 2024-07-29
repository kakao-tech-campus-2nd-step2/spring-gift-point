package gift.auth.controller;

import gift.auth.dto.LoginReqDto;
import gift.auth.service.AuthService;
import gift.auth.token.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 API", description = "로그인 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
            })
    public ResponseEntity<AuthToken> login(@RequestHeader("Authorization") String header, @RequestBody LoginReqDto loginReqDto) {
        AuthToken token = authService.login(header, loginReqDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 소셜 로그인", description = "카카오 로그인을 통해 인증하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
            })
    public ResponseEntity<AuthToken> login(@RequestParam("code") String code) {
        AuthToken token = authService.login(code);
        return ResponseEntity.ok(token);
    }
}
