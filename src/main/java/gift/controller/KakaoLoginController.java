package gift.controller;

import gift.constants.SuccessMessage;
import gift.properties.KakaoProperties;
import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/members")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin() {
        String clientId = kakaoProperties.getClientId();
        String redirectUrl = kakaoProperties.getRedirectUrl();
        String authorizeUrl = kakaoProperties.getAuthorizeUrl();

        String loginUrl = String.format(
            authorizeUrl + "?scope=talk_message&response_type=code&redirect_uri=%s&client_id=%s",
            redirectUrl,
            clientId
        );

        return new RedirectView(loginUrl);
    }

    @ResponseBody
    @GetMapping("/login/kakao/oauth/token")
    public ResponseEntity<String> kakaoOauthToken(@RequestParam("code") String code) {
        String token = kakaoService.kakaoLogin(code);

        return ResponseEntity.ok().header("token", token)
            .body(SuccessMessage.LOGIN_MEMBER_SUCCESS_MSG);
    }
}
