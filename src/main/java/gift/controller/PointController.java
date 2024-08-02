package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.PointChargeRequestDto;
import gift.dto.PointResponseDto;
import gift.entity.User;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@Tag(name = "Points", description = "포인트 관련 API")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PointResponseDto> getPoints(@LoginMember User loginUser) {
        PointResponseDto dto = pointService.getUserPoints(loginUser.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "관리자가 사용자의 포인트를 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 포인트가 0보다 작은 경우"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PointResponseDto> chargePoints(
            @Parameter(description = "포인트 충전 요청 정보", required = true) @RequestBody PointChargeRequestDto requestDto) {
        PointResponseDto dto = pointService.chargePoints(requestDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
