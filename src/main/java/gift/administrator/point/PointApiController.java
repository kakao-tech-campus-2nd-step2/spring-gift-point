package gift.administrator.point;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@Tag(name = "point API", description = "point related API")
public class PointApiController {

    private final PointService pointService;

    public PointApiController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/add")
    @Operation(summary = "add points", description = "포인트를 추가합니다.")
    public ResponseEntity<ApiResponse<PointResponse>> addPoints(@RequestBody PointResponse pointRequest){
        pointService.addPoints(pointRequest.getUserId(), pointRequest.getPoints());
        PointResponse pointResponse = new PointResponse(pointRequest.getUserId(), pointRequest.getPoints());
        ApiResponse<PointResponse> apiResponse = new ApiResponse<>(HttpResult.OK, "포인트 추가 성공",
            HttpStatus.OK, pointResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/use")
    @Operation(summary = "use points", description = "포인트를 사용합니다.")
    public ResponseEntity<ApiResponse<PointResponse>> usePoints(@RequestBody PointResponse pointRequest){
        int points = pointService.usePoints(pointRequest.getUserId(), pointRequest.getPoints());
        PointResponse pointResponse = new PointResponse(pointRequest.getUserId(), points);
        ApiResponse<PointResponse> apiResponse = new ApiResponse<>(HttpResult.OK, "포인트 사용 성공",
            HttpStatus.OK, pointResponse);
        return ResponseEntity.ok(apiResponse);
    }
}
