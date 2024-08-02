package gift.users.user;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "user API", description = "user related API")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "user register", description = "이메일과 비밀번호로 회원가입을 진행합니다. required info(email and password)")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody UserDTO user) {
        UserDTO result = userService.register(user);
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "회원가입 성공",
            HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "user login", description = "이메일과 비밀번호로 로그인을 진행합니다. required info(email and password)")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserDTO user) {
        String token = userService.login(user);
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpResult.OK, "로그인 성공", HttpStatus.OK,
            token);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
