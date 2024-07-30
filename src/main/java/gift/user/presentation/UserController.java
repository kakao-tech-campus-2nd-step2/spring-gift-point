package gift.user.presentation;

import gift.user.application.UserService;
import gift.user.domain.User;
import gift.user.domain.UserRegisterRequest;
import gift.util.CommonResponse;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "유저 등록", description = "새로운 유저를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(new CommonResponse<>(null, "유저 등록이 정상적으로 완료되었습니다", true));
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
        String tokenResponse = jwtUtil.generateToken(user, 60L);
        return ResponseEntity.ok(new CommonResponse<>(tokenResponse, "로그인이 정상적으로 완료되었습니다", true));
    }

    public static class AuthenticationResponse {

        private final String jwt;

        public AuthenticationResponse(String jwt) {
            this.jwt = jwt;
        }

        public String getJwt() {
            return jwt;
        }
    }
}
