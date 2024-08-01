package gift.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    @GetMapping("/register")
    public String register() {
        return "register_form";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
