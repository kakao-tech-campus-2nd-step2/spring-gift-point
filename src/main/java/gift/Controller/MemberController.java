package gift.Controller;

import gift.Entity.Member;
import gift.Model.MemberDto;
import gift.Service.MemberService;
import gift.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Controller
@Tag(name = "Member", description = "회원 관련 api")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지 전환", description = "로그인 페이지로 전환합니다.")
    public String login(Model model) {
        model.addAttribute("user", new MemberDto());
        return "login";
    }

    @PostMapping(value = "/login")
    @Operation(summary = "로그인 수행", description = "로그인을 수행합니다.")
    public String login(@ModelAttribute MemberDto memberDto, Model model, HttpServletResponse response) {
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();

        boolean isAuthenticated = memberService.authenticate(email, password);
        if (isAuthenticated) {
            boolean isAdmin = memberService.isAdmin(email);
            Optional<MemberDto> authenticatedMember = memberService.findByEmail(email);
            String token = jwtUtil.generateToken(authenticatedMember.get(), isAdmin);
            // Set token in HttpOnly cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            if (isAdmin) {
                return "redirect:/api/products";
            }
            return "redirect:/products";
        }

        model.addAttribute("error", "Authentication failed");
        return "login";
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "회원가입", description = "회원가입 페이지를 보여주고 회원가입을 수행합니다.")
    public String register(@ModelAttribute MemberDto memberDto, Model model, HttpServletRequest request) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("user", new MemberDto());
            return "register";
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("user", memberDto);
            memberService.register(memberDto);
            model.addAttribute("message", "회원가입에 성공했습니다.");
            return "login";
        }
        return "login";
    }
}
