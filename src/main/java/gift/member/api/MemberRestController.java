package gift.member.api;

import gift.auth.application.AuthService;
import gift.auth.dto.AuthResponse;
import gift.member.application.MemberService;
import gift.member.dto.MemberRequest;
import gift.member.dto.PointRequest;
import gift.member.entity.Member;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public AuthResponse signUp(@RequestBody @Valid MemberRequest memberRequest) {
        return authService.generateAuthResponse(
                memberService.registerMember(memberRequest)
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid MemberRequest memberRequest) {
        return authService.authenticate(memberRequest);
    }

    @PutMapping("/{memberId}/point")
    public void chargeMemberPoint(@PathVariable("memberId") Long memberId,
                                  @RequestBody @Valid PointRequest request) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        memberService.saveMemberPoint(member, request.point());
    }

}
