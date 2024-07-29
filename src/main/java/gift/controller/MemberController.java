package gift.controller;

import gift.dto.LogInMemberDTO;
import gift.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signin")
    public String signinController(@RequestBody LogInMemberDTO member) {
        return memberService.signin(member);
    }

    @PostMapping("/login")
    public String loginController(@RequestBody LogInMemberDTO LonInMember) {
        return memberService.login(LonInMember);
    }
}