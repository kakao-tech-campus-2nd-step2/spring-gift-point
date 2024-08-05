package gift.user.controller;

import gift.common.annotation.LoginUser;
import gift.user.dto.request.ChargePointRequest;
import gift.user.dto.response.PointResponse;
import gift.user.entity.User;
import gift.user.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getUserPoint(@LoginUser User user) {
        return ResponseEntity.ok(pointService.getPoint(user));
    }

    @PostMapping
    public ResponseEntity<PointResponse> chargeUserPoint(@RequestBody @Valid ChargePointRequest request) {
        return ResponseEntity.ok(pointService.chargePoint(request));
    }

}
