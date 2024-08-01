package gift.controller.user;

import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import gift.controller.user.dto.UserRequest;
import gift.controller.user.dto.UserResponse;
import gift.controller.user.dto.UserResponse.Point;
import gift.service.UserDto;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 시도합니다.")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequest.Create request) {
        Long id = userService.register(request);
        return ResponseEntity.created(URI.create("/api/members/" + id)).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 시도합니다.")
    public ResponseEntity<UserResponse.Login> login(@Valid @RequestBody UserRequest.Login request) {
        UserDto response = userService.login(request);
        return ResponseEntity.ok()
            .header("Authorization", response.accessToken())
            .body(UserResponse.Login.from(response.name()));
    }

    @GetMapping("/point")
    public ResponseEntity<UserResponse.Point> getUserPoint(@LoginUser LoginInfo user) {
        UserResponse.Point response = userService.getPoint(user.id());
        return ResponseEntity.ok(response);
    }
}
