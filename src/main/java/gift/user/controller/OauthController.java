package gift.user.controller;

import gift.user.model.dto.KakaoTokenResponse;
import gift.user.service.JwtUserService;
import gift.user.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth")
public class OauthController {
    private final KakaoService kakaoService;
    private final JwtUserService jwtUserService;

    public OauthController(KakaoService kakaoService, JwtUserService jwtUserService) {
        this.kakaoService = kakaoService;
        this.jwtUserService = jwtUserService;
    }

    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin() {
        return new RedirectView(kakaoService.buildAuthorizeUrl());
    }

    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse token = kakaoService.getAccessToken(code);
        String email = kakaoService.getUserInfo(token.accessToken());
        String jwt = jwtUserService.loginOauth(email, token.accessToken());

        if (jwt == null) {
            return ResponseEntity.badRequest().body("회원가입이 필요합니다.");
        }

        return ResponseEntity.ok()
                .header("Authorization", jwt)
                .body("로그인 성공");
    }
}

