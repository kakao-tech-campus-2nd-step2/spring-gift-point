package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.point.PointRequestDTO;
import gift.dto.point.PointResponseDTO;
import gift.model.User;
import gift.service.PointService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/points")
public class PointController {
    private final UserService userService;
    private final PointService pointService;

    public PointController(UserService userService, PointService pointService) {
        this.userService = userService;
        this.pointService = pointService;
    }

    @GetMapping
    @Operation(summary = "사용자 포인트 조회",
            description = "로그인한 사용자의 현재 포인트 잔액을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "포인트 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    public ResponseEntity<PointResponseDTO> getPoint(@LoginUser User user) {
        PointResponseDTO pointResponseDTO = userService.findPoint(user.getId());

        return new ResponseEntity<>(pointResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    @GetMapping
    @Operation(summary = "사용자 포인트 충전",
            description = "로그인한 사용자에게 지정된 양의 포인트를 충전합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "포인트 충전을 성공적으로 완료"),
                    @ApiResponse(responseCode = "400", description = "포인트 부족"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 접근"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    public ResponseEntity<PointResponseDTO> chargePoint(@LoginUser User user, @Valid @RequestBody PointRequestDTO pointRequestDTO) {
        PointResponseDTO pointResponseDTO = pointService.chargePoint(user.getId(), pointRequestDTO.point());

        return new ResponseEntity<>(pointResponseDTO, HttpStatus.OK);
    }

}
