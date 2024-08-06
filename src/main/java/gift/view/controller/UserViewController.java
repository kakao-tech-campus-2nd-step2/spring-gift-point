package gift.view.controller;

import gift.user.dto.UserInfo;
import gift.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(
        Pageable pageable,
        Model model
    ) {
        Page<UserInfo> users = userService.getAllUsers(pageable);

        model.addAttribute("userPage", users);
        model.addAttribute("maxPage", 10);
        return "user_list";
    }

    @GetMapping("/register")
    public String register() {
        return "register_form";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
