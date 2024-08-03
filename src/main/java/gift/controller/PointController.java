package gift.controller;

import gift.dto.PointRequest;
import gift.dto.PointResponse;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@Tag(name = "Point API", description = "포인트 관련 API")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    @Operation(summary = "포인트 조회", description = "멤버의 포인트를 조회한다.")
    public ResponseEntity<PointResponse> getPoint(@RequestHeader("Authorization") String token) {
        String authToken = token.substring(7);
        Long points = pointService.getPoints(authToken);

        PointResponse response = new PointResponse(points);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{member_id}")
    @Operation(summary = "포인트 충전", description = "멤버의 포인트를 충전한다.")
    public ResponseEntity<PointResponse> chargePoints(@RequestHeader("Authorization") String token,
        @PathVariable("member_id") Long memberId, @RequestBody PointRequest pointRequest) {
        String authToken = token.substring(7);
        Long updatedPoints = pointService.chargePoints(authToken, memberId, pointRequest);

        PointResponse response = new PointResponse(updatedPoints);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
