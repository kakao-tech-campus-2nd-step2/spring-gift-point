package gift.controller;

import gift.dto.memberDTOs.LogInMemberDTO;
import gift.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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