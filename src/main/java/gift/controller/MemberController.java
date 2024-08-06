package gift.controller;

import gift.domain.member.MemberRequest;
import gift.domain.member.TokenResponse;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody MemberRequest memberRequest) {
        return new TokenResponse(memberService.register(memberRequest));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody MemberRequest memberRequest) {
        return new TokenResponse(memberService.login(memberRequest));
    }
}
