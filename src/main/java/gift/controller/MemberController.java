package gift.controller;

import gift.dto.LoginDTO;
import gift.dto.MemberDTO;
import gift.service.MemberService;
import gift.util.JwtUtil;
import gift.model.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@Tag(name = "멤버 관리 API", description = "멤버(사용자) 관리를 위한 API")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/new")
    @Operation(summary = "회원 가입 폼 보기", description = "회원 가입 폼으로 이동합니다.")
    public String showRegistrationForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO("", "", ""));
        return "register_member_form";
    }

    @PostMapping
    @Operation(summary = "회원 가입", description = "회원 가입합니다.")
    public String registerUser(@ModelAttribute @Valid MemberDTO memberDTO, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            return "register_member_form";
        }

        Member existingMember = memberService.findMemberByEmail(memberDTO.email());
        if (existingMember != null) {
            model.addAttribute("emailError", "이메일이 이미 존재합니다.");
            return "register_member_form";
        }

        Member member = new Member(null, memberDTO.name(), memberDTO.email(), memberDTO.password(),
            "user");
        memberService.saveMember(memberDTO);

        return "register_success";
    }

    @GetMapping("/auth")
    @Operation(summary = "로그인 폼 보기", description = "로그인 폼으로 이동합니다.")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO("", ""));
        return "login_member_form";
    }

    @PostMapping("/auth")
    @Operation(summary = "로그인", description = "로그인합니다.")
    public String loginUser(@ModelAttribute @Valid LoginDTO loginDTO, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            return "login_member_form";
        }

        Member member = memberService.findMemberByEmail(loginDTO.email());
        if (member == null || !member.getPassword().equals(loginDTO.password())) {
            model.addAttribute("loginError", "잘못된 이메일 또는 비밀번호입니다.");
            return "login_member_form";
        }

        String token = jwtUtil.generateToken(member.getEmail(), member.getRole());
        model.addAttribute("token", token);

        return "login_success";
    }
}