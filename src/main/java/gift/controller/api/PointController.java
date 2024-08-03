package gift.controller.api;

import gift.dto.response.PointResponse;
import gift.interceptor.MemberId;
import gift.service.PointService;
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
    public ResponseEntity<PointResponse> getMemberPoint(@MemberId Long memberId) {
        PointResponse pointResponse = pointService.getMemberPoint(memberId);
        return ResponseEntity.ok(pointResponse);
    }
}
