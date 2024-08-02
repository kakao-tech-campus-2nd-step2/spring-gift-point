package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/members")
public class MemberViewController {

    @GetMapping("/register")
    public String registerMemberForm() {
        return "memberRegister";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
