package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.point.PointRequestDTO;
import gift.dto.point.PointResponseDTO;
import gift.model.User;
import gift.service.PointService;
import gift.service.UserService;
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
    public ResponseEntity<PointResponseDTO> getPoint(@LoginUser User user) {
        PointResponseDTO pointResponseDTO = userService.findPoint(user.getId());

        return new ResponseEntity<>(pointResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PointResponseDTO> chargePoint(@LoginUser User user, @Valid @RequestBody PointRequestDTO pointRequestDTO) {
        PointResponseDTO pointResponseDTO = pointService.chargePoint(user.getId(), pointRequestDTO.point());

        return new ResponseEntity<>(pointResponseDTO, HttpStatus.OK);
    }

}
