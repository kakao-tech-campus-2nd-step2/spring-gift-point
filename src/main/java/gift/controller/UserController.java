package gift.controller;

import gift.CustomAnnotation.RequestRole;
import gift.model.entity.Role;
import gift.model.response.PointResponse;
import gift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestRole(Role.ROLE_USER)
    @GetMapping("/points")
    public ResponseEntity<PointResponse> getUserPoint(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(userService.getPointFromUser(userId));
    }
}
