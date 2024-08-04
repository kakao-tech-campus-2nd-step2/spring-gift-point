package gift.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping("/members/login")
    @Deprecated
    public String loginPage() {
        return "login/page";
    }
}
