package gift.point;

import gift.common.auth.LoginMember;
import gift.common.auth.LoginMemberDto;
import gift.point.model.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/api/points")
    public ResponseEntity<PointResponse> getPoint(@LoginMember LoginMemberDto loginMemberDto) {
        return ResponseEntity.ok(pointService.getPoint(loginMemberDto));
    }
}
