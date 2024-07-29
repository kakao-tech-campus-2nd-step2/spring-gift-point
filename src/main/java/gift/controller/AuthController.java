package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public String main() {
        return "auth/index";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign-up";
    }

    @PostMapping("/save")
    public ResponseEntity<MemberResponseDto> save(@ModelAttribute MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.save(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@ModelAttribute MemberRequestDto memberRequestDto) throws AuthenticationException {
        if (memberService.login(memberRequestDto.getEmail(), memberRequestDto.getPassword())) {
            return new ResponseEntity<>(memberService.generateTokenFrom(memberRequestDto.getEmail()), HttpStatus.OK);
        }
        throw new AuthenticationException("로그인 실패");
    }
}
