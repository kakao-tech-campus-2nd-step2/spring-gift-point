package gift.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthPageController {

    @GetMapping("/oauth/login")
    public String admin() {
        return "oauthLoginPage";
    }

}
