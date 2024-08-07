package gift.controller;


import gift.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/members/v2")

@Tag(name = "login API", description = "회원 관련 API")
public class MemberController {

    @GetMapping("/register")
    @Operation(summary = "회원가입 폼", description = "회원가입 폼을 반환합니다.")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }


    @GetMapping("/login")
    @Operation(summary = "로그인 폼", description = "로그인 폼을 반환합니다.")
    public String showLoginForm() {
        return "login";
    }


}
