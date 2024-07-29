package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUrl;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUrl", redirectUrl);
        return "login";
    }
}