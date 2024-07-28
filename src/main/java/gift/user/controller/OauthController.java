package gift.user.controller;

import gift.user.model.dto.KakaoTokenResponse;
import gift.user.service.JwtUserService;
import gift.user.service.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth")
public class OauthController {
    private final KakaoLoginService kakaoLoginService;
    private final JwtUserService jwtUserService;

    public OauthController(KakaoLoginService kakaoLoginService, JwtUserService jwtUserService) {
        this.kakaoLoginService = kakaoLoginService;
        this.jwtUserService = jwtUserService;
    }

    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin() {
        return new RedirectView(kakaoLoginService.buildAuthorizeUrl());
    }

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

