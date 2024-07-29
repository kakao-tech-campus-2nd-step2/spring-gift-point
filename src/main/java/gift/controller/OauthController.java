package gift.controller;

import gift.dto.oauth.KakaoTokenResponse;
import gift.service.JwtUserService;
import gift.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth")
@Tag(name = "Oauth", description = "Oauth Login API")
public class OauthController {
    private final KakaoLoginService kakaoLoginService;
    private final JwtUserService jwtUserService;

    public OauthController(KakaoLoginService kakaoLoginService, JwtUserService jwtUserService) {
        this.kakaoLoginService = kakaoLoginService;
        this.jwtUserService = jwtUserService;
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 페이지로 리다이렉트")
    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin() {
        return new RedirectView(kakaoLoginService.buildAuthorizeUrl());
    }

    @Operation(summary = "카카오 로그인 리다이렉트", description = "카카오 로그인 후 jwt 토큰 발급")
    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse token = kakaoLoginService.getAccessToken(code);
        String email = kakaoLoginService.getUserInfo(token.accessToken());
        String jwt = jwtUserService.loginOauth(email, token.accessToken());

        if (jwt == null) {
            return ResponseEntity.badRequest().body("회원가입이 필요합니다.");
        }

        return ResponseEntity.ok()
                .header("Authorization", jwt)
                .body("로그인 성공");
    }
}

