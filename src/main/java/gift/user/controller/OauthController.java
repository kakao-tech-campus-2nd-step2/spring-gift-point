package gift.user.controller;

import gift.user.oauth.KakaoAuthToken;
import gift.user.oauth.KakaoProperties;
import gift.user.oauth.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth")
public class OauthController {
    // @RequestMapping("/kakao")
    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public OauthController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin() {
        return new RedirectView(kakaoService.buildAuthorizeUrl());
    }

    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        KakaoAuthToken token = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(token.accessToken());
    }

}

