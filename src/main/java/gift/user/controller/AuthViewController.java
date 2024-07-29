package gift.user.controller;


import gift.user.domain.User;
import gift.user.domain.dto.LoginRequest;
import gift.user.domain.dto.LoginResponse;
import gift.user.service.UserService;
import gift.utility.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class AuthViewController {
    private final UserService userService;

    public AuthViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new LoginRequest("", ""));
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute LoginRequest loginRequest) {
        userService.save(new User(0L, loginRequest.email(), loginRequest.password()));
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new LoginRequest("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model) {
        User user = userService.findByEmail(loginRequest.email())
            .filter(u -> loginRequest.password().equals(u.getPassword()))
            .orElse(null);
        if (user == null) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        String token = JwtUtil.generateToken(user.getEmail());
        model.addAttribute("token", token);
        return "redirect:/wishlist/" + user.getId();
    }
}
