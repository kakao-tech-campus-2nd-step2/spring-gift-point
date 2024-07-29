package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
@PropertySource("classpath:application-dev.properties")
public class LoginViewController {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        model.addAttribute("redirectUri", "http://localhost:8080/login/code");
        return "login";
    }


}
