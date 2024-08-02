package gift.controller;

import gift.dto.MemberDto;
import gift.entity.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/members")
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    @Operation(summary = "회원가입 폼", description = "회원가입 폼을 반환합니다.")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
    public String register(@ModelAttribute("member") MemberDto memberDto, HttpServletResponse response) {
        String token = memberService.registerMember(memberDto);
        response.setHeader("Authorization", "Bearer " + token);
        return "redirect:/members/login";
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 폼", description = "로그인 폼을 반환합니다.")
    public String showLoginForm() {
        return "login";
    }

}
