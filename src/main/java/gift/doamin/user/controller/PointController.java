package gift.doamin.user.controller;

import gift.doamin.user.dto.PointAddRequest;
import gift.doamin.user.dto.PointResponse;
import gift.doamin.user.dto.UserDto;
import gift.doamin.user.service.PointService;
import gift.global.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public PointResponse getPoint(@LoginUser UserDto user) {
        return pointService.getPoint(user.getId());
    }

    @PostMapping
    public PointResponse addPoint(@LoginUser UserDto user,
        @RequestBody PointAddRequest pointAddRequest) {
        return pointService.addPoint(user.getId(), pointAddRequest.getPoint());
    }


}
