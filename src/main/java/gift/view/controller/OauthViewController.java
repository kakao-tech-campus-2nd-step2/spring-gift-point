package gift.view.controller;

import gift.common.client.kakao.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class OauthViewController {

    private final KakaoProperties kakaoProperties;

    public OauthViewController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/kakao/login")
    public String loginKakao() {
        String authorizationUrl = "https://kauth.kakao.com/oauth/authorize"
            + "?scope=talk_message"
            + "&response_type=code"
            + "&redirect_uri=" + kakaoProperties.redirectUri()
            + "&client_id=" + kakaoProperties.clientId();

        return "redirect:" + authorizationUrl;
    }

    @GetMapping("/kakaoLoginSuccess")
    public String kakaoLoginSuccess(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "kakao_login_success";
    }
}
