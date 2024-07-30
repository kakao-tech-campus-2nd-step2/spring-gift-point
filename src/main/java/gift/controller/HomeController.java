package gift.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        // 세션에서 액세스 토큰 가져오기
        String accessToken = (String) request.getSession().getAttribute("accessToken");

        if (accessToken != null) {
            model.addAttribute("message", "Welcome to the Home Page!");
            model.addAttribute("accessToken", accessToken);
        } else {
            model.addAttribute("message", "로그인을 먼저 해주세요.");
        }

        return "home"; // home.html 보여주기
    }
}