package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao")
public class KakaoLoginPageController {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @GetMapping("/login")
    public String ShowLoginPage(Model model) {
        String url =
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId
                + "&redirect_uri=" + redirectUri;
        model.addAttribute("url", url);
        return "login";
    }

}
