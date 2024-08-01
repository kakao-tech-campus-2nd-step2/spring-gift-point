package gift.view.controller;

import gift.client.kakao.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private final KakaoProperties kakaoProperties;

    public UserViewController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/register")
    public String register() {
        return "register_form";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
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
