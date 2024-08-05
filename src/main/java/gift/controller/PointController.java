package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.PointRequest;
import gift.dto.response.PointResponse;
import gift.exception.CustomException;
import gift.service.PointService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "point", description = "포인트 API")
@RequestMapping("/api/point")
public class PointController {

    private PointService pointService;
    private JwtUtil jwtUtil;

    public PointController(PointService pointService, JwtUtil jwtUtil){
        this.pointService = pointService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping
    @Operation(summary = "포인트 조회", description = "회원의 포인트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "401", description = "토큰 오류")
    })
    public ResponseEntity<PointResponse> getPoint(@RequestHeader("Authorization") String authorizationHeader){
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }
        return pointService.getPoint(jwtUtil.extractToken(authorizationHeader));
    }

    @PostMapping("/{memberId}")
    @Operation(summary = "포인트 충전", description = "회원의 포인트를 충전합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "충천 성공"),
        @ApiResponse(responseCode = "401", description = "토큰 오류")
    })
    public ResponseEntity<PointResponse> chargePoint(@RequestHeader("Authorization") String authorizationHeader,@RequestBody PointRequest pointRequest, @PathVariable Long memberId){
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }
        return pointService.chargePoint(jwtUtil.extractToken(authorizationHeader), pointRequest, memberId);
    }
}
