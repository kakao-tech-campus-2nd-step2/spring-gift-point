package gift.controller;

import gift.model.user.User;
import gift.model.user.AuthenticationRequest;
import gift.model.user.AuthenticationResponse;
import gift.model.user.RegisterRequest;
import gift.service.UserService;
import gift.common.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 API", description = "로그인 관련 API")
@RestController
@RequestMapping("/members")
public class AuthController {
    private final UserService userService;
    private JwtUtil jwtUtil;
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "사용자 등록", description = "사용자 정보와 토큰을 받아서 등록한다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @Operation(summary = "로그인", description = "사용자 정보와 저장된 정보를 비교해서 로그인을 진행한다.")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
        User user = userService.valid(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
