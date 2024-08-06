package gift.doamin.user.controller;

import gift.doamin.user.dto.PointAddRequest;
import gift.doamin.user.dto.PointResponse;
import gift.doamin.user.dto.UserDto;
import gift.doamin.user.service.PointService;
import gift.global.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트", description = "포인트 조회, 충전 API")
@RestController
@RequestMapping("/api/point")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @Operation(summary = "포인트 조회", description = "로그인한 사용자의 포인트를 조회합니다.")
    @GetMapping
    public PointResponse getPoint(@LoginUser UserDto user) {
        return pointService.getPoint(user.getId());
    }

    @Operation(summary = "포인트 충전", description = "로그인한 사용자의 포인트를 충전합니다.")
    @PostMapping
    public PointResponse addPoint(@LoginUser UserDto user,
        @Valid @RequestBody PointAddRequest pointAddRequest) {
        return pointService.addPoint(user.getId(), pointAddRequest.getPoint());
    }


}
