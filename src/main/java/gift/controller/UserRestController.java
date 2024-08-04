package gift.controller;

import gift.common.annotation.LoginMember;
import gift.model.user.PointResponse;
import gift.model.user.User;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 API", description = "유저 관련 API")
@RestController
@RequestMapping("api/members")
public class UserRestController {
    private final UserService userService;
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "email별 유저 조회", description = "email을 받아서 유저를 조회한다.")
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "user의 포인트 조회", description = "user의 포인트를 조회한다.")
    @GetMapping("/me")
    public ResponseEntity<PointResponse> getPointByUser(@LoginMember User user) {
        PointResponse response = userService.getPoint(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "user의 포인트 충전", description = "user의 포인트를 충전한다.")
    @PostMapping("/me/point")
    public ResponseEntity<PointResponse> addPointToUser(@LoginMember User user, @RequestBody int point) {
        userService.addPoint(user, point);
        PointResponse response = userService.getPoint(user);
        return ResponseEntity.ok(response);
    }

}
