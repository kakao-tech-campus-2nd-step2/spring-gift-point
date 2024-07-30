package gift.domain.user.controller;

import gift.auth.jwt.JwtToken;
import gift.domain.user.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/login")
@Tag(name = "OAuth Login", description = "OAuth 로그인 API")
public class OauthLoginController {

    private final KakaoLoginService kakaoLoginService;

    public OauthLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 주소로 redirect 합니다.")
    public void getAuthCodeUrl(
        @Parameter(hidden = true)
        HttpServletResponse response
    ) throws IOException {
        response.sendRedirect(kakaoLoginService.getAuthCodeUrl());
    }

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "콜백을 처리해 카카오 계정으로 로그인합니다.")
    public ResponseEntity<JwtToken> login(
        @Parameter(description = "카카오 로그인 API에서 전달된 인증 코드")
        @RequestParam("code") String code
    ) {
        JwtToken jwtToken = kakaoLoginService.login(code);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
