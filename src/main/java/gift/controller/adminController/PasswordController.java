package gift.controller.adminController;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    @Value("${admin.password}")
    private String PASSWORD;

    @GetMapping("/")
    public String showIndex(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            return "redirect:/landingPage";
        }
        return "landing-password";
    }

    @PostMapping("/verifyPassword")
    public String verifyPassword(@RequestParam String password, Model model, HttpSession session) {
        if (PASSWORD.equals(password)) {
            session.setAttribute("authenticated",true);
            return "redirect:/";
        }
        model.addAttribute("error","Incorrect Password. Try Again");
        return "landing-password";
    }

    @GetMapping("/landingPage")
    public String showLandingPageAfter(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("authenticated"))) {
            return "index";
        }
        return "redirect:/";
    }
}
