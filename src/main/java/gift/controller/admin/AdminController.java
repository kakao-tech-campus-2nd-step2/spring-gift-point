package gift.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Operation(summary = "관리자 페이지", description = "관리자 페이지를 보여줍니다.")
    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }
}
