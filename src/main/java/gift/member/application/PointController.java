package gift.member.application;

import gift.auth.resolver.LoginMember;
import gift.member.application.dto.response.MemberPointResponse;
import gift.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberPointResponse> getPoint(@LoginMember Long memberId) {
        var point = memberService.getPoint(memberId);

        var response = MemberPointResponse.from(point);

        return ResponseEntity.ok()
                .body(response);
    }
}
