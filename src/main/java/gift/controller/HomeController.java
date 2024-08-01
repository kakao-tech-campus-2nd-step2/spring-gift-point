package gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name="기본 home 화면", description = "프론트 연결 때는 쓰이지 않음")
public class HomeController {

    @Operation(summary = "로컬호스트 접속")
    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
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