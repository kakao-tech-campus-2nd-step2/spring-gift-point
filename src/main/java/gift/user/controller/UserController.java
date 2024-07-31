package gift.user.controller;

import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.service.KakaoUserService;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저 관리", description = "유저 회원가입 및 로그인 API")
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    @PostMapping("register")
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("login")
    @Operation(summary = "로그인", description = "사용자가 로그인합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<UserResponse> login(@RequestBody @Valid UserLoginRequest userRequest) {
        return ResponseEntity.ok(userService.loginUser(userRequest));
    }

    @RequestMapping("auth/kakao/code")
    @Hidden
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String code,
        HttpServletResponse response) throws IOException {
        var kakaoResponse = kakaoUserService.loginKakaoUser(code);
        String redirectUrl = "/users/kakaoLoginSuccess?token=" + kakaoResponse.token();
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

}
