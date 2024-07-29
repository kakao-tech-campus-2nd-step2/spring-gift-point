package gift.users.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "user API", description = "user related API")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "user register", description = "이메일과 비밀번호로 회원가입을 진행합니다. required info(email and password)")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user) {
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입되었습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "user login", description = "이메일과 비밀번호로 로그인을 진행합니다. required info(email and password)")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {
        String token = userService.login(user);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
