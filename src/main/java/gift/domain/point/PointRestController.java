package gift.domain.point;

import gift.domain.member.dto.LoginInfo;
import gift.global.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Point", description = "Point API")
public class PointRestController {
    private final PointService pointService;
    public PointRestController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/api/points")
    @Operation(summary = "내 포인트 조회")
    public ResponseEntity<PointRequestResponse> getPoint(@Login LoginInfo loginInfo) {
        int point = pointService.getPoint(loginInfo.getId());
        return ResponseEntity.ok(new PointRequestResponse(point));
    }

    @PostMapping("/api/points")
    @Operation(summary = "내 포인트 충전")
    public ResponseEntity chargePoint(
        @Login LoginInfo loginInfo, @Valid @RequestBody PointRequestResponse pointRequestResponse) {
        pointService.chargePoint(loginInfo.getId(), pointRequestResponse.point());
        return ResponseEntity.ok().build();
    }
}
