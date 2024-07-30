package gift.member.api;

import gift.auth.application.AuthService;
import gift.auth.dto.AuthResponse;
import gift.member.application.MemberService;
import gift.member.dto.MemberDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberRestController(MemberService memberService,
                                AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public void signUp(@RequestBody @Valid MemberDto memberDto) {
        memberService.registerMember(memberDto);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid MemberDto memberDto) {
        return authService.authenticate(memberDto);
    }


}
