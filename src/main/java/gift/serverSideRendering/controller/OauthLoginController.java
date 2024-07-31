package gift.serverSideRendering.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OauthLoginController {

    @Value("${oauth.kakao.api_key}")
    private String KAKAO_API_KEY;

    @Value("${oauth.kakao.redirect_uri}")
    private String REDIRECT_URI;

    @GetMapping("/oauth/login")
    public String login(Model model) {
        model.addAttribute("kakaoApiKey", KAKAO_API_KEY);
        model.addAttribute("redirectUri", REDIRECT_URI);
        return "kakaoLogin";
    }
}
