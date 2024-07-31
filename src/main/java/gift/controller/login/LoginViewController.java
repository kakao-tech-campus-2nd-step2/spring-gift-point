package gift.controller.login;

import gift.DTO.member.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginViewController {

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "login";
    }
}
