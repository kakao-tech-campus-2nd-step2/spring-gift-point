package gift.controller;

import gift.service.PointService;
import gift.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@Tag(name = "Point API", description = "포인트 API 관련 엔드포인트")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    @Operation(summary = "포인트 충전", description = "포인트를 충전합니다.")
    public ResponseEntity<String> addPoints(@RequestBody int point, @LoginMember Member member) {
        try {
            pointService.addPoint(member.getId(), point);
            return ResponseEntity.status(HttpStatus.CREATED).body("포인트 충전 완료!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "포인트 조회", description = "회원의 포인트를 조회합니다.")
    public ResponseEntity<Integer> getPoints(@LoginMember Member member) {
        try {
            int points = pointService.getPoint(member.getId());
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
        }
    }

}
