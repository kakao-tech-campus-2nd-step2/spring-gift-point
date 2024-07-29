package gift.controller.user;

import gift.controller.user.dto.UserRequest;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 시도합니다.")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequest.Create request) {
        Long id = userService.register(request);
        return ResponseEntity.created(URI.create("/api/v1/user/" + id)).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 시도합니다.")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequest.Update request) {
        String token = userService.login(request);
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(token);
    }
}
