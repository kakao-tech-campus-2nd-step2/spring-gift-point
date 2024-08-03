package gift.controller;

import gift.dto.PointRequest;
import gift.dto.PointResponse;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/points")
public class PointController {
    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<PointResponse> addPoint(@PathVariable Long user_id, @RequestBody PointRequest pointRequest) {
        PointResponse pointResponse = memberService.addPoint(user_id, pointRequest );
        return ResponseEntity.ok(pointResponse);
    }
}
