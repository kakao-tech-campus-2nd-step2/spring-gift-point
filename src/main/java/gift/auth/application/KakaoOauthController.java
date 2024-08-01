package gift.auth.application;

import gift.auth.application.dto.OAuthResponse;
import gift.auth.service.KakaoOAuthService;
import gift.auth.service.facade.OAuthFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOauthController {
    private final OAuthFacade oAuthFacade;
    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOauthController(OAuthFacade oAuthFacade, KakaoOAuthService kakaoOAuthService) {
        this.oAuthFacade = oAuthFacade;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping
    public ResponseEntity<Void> getOauthURL() {
        var kakaoLoginUrl = kakaoOAuthService.getKakaoLoginUrl();
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("location", kakaoLoginUrl)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<OAuthResponse> callBack(@RequestParam("code") String code) {
        var memberSignInInfo = oAuthFacade.kakaoCallBack(code);

        var response = new OAuthResponse(memberSignInInfo.token());
        return ResponseEntity.ok()
                .body(response);
    }
}
