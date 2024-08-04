package gift.member.presentation.restcontroller;

import gift.docs.member.PointApiDocs;
import gift.global.authentication.annotation.MemberId;
import gift.member.business.service.MemberService;
import gift.member.presentation.dto.PointRequest;
import gift.member.presentation.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController implements PointApiDocs {

    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> addPoint(
        @MemberId Long memberId,
        @RequestBody PointRequest.Init pointRequestInit) {
        var point =  pointRequestInit.point();
        memberService.addPoint(memberId, point);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PointResponse.Init> getPoint(
        @MemberId Long memberId) {
        var point = memberService.getPoint(memberId);
        return ResponseEntity.ok(new PointResponse.Init(point));
    }
}
