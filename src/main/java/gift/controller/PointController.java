package gift.controller;

import gift.dto.point.PointRequest;
import gift.dto.point.PointResponse;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Point Management System", description = "Operation related to point management")
public class PointController {
    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/members/points")
    @Operation(summary = "Get member's point", description = "Fetches the points balance for a member")
    public ResponseEntity<PointResponse> getPoints(@RequestParam Long memberId) {
        PointResponse pointResponse = pointService.getPoints(memberId);
        return ResponseEntity.ok(pointResponse);
    }

    @PostMapping("/members/points")
    @Operation(summary = "Add points to a member", description = "Adds points to a member")
    public ResponseEntity<PointResponse> addPoints(@RequestBody PointRequest pointRequest) {
        PointResponse pointResponse = pointService.addPoint(pointRequest);
        return ResponseEntity.ok(pointResponse);
    }

    @PostMapping("/orders")
    @Operation(summary = "Use points to an order", description = "Use points to an order")
    public ResponseEntity<PointResponse> usePoints(@RequestBody PointRequest pointRequest) {

    }
}
