package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.dto.PointResponseDto;
import gift.domain.model.entity.User;
import gift.service.PointService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
@Validated
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<PointResponseDto> getPoints(@LoginUser User user) {
        return ResponseEntity.ok(pointService.getPoints(user));
    }
}
