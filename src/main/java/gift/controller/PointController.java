package gift.controller;

import gift.dto.PointRequest;
import gift.dto.PointResponse;
import gift.model.LoginMember;
import gift.model.Member;
import gift.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/points")
@RestController
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PutMapping
    public ResponseEntity<Void> chargePoint(@Valid @RequestBody PointRequest request, @LoginMember Member member) {
        pointService.pointCharge(request, member);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@LoginMember Member member) {
        PointResponse response = pointService.getPoint(member);
        return ResponseEntity.ok().body(response);
    }
}
