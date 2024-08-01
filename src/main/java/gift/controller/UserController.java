package gift.controller;

import gift.domain.AppUser;
import gift.dto.common.CommonResponse;
import gift.dto.user.LoginRequest;
import gift.dto.user.LoginResponse;
import gift.dto.user.SignUpRequest;
import gift.dto.user.SignUpResponse;
import gift.dto.user.UpdatePasswordRequest;
import gift.exception.user.ForbiddenException;
import gift.service.JwtUserService;
import gift.service.UserService;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;
    private final JwtUserService jwtUserService;

    public UserController(UserService userService, JwtUserService jwtUserService) {
        this.userService = userService;
        this.jwtUserService = jwtUserService;
    }

    @Operation(summary = "회원가입", description = "회원가입 후 로그인 필요함")
    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        AppUser user = userService.signUp(signUpRequest);
        String token = jwtUserService.getToken(user.getId());
        SignUpResponse response = new SignUpResponse(signUpRequest.getEmail(), token);
        return ResponseEntity.status(HttpStatus.CREATED).header("Authorization", token)
                .body(new CommonResponse<>(response, "회원가입이 완료되었습니다.", true));
    }

    @Operation(summary = "로그인", description = "로그인 성공 후 jwt 토큰 발급")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = jwtUserService.login(loginRequest);
        LoginResponse response = new LoginResponse(loginRequest.email(), token);
        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(new CommonResponse<>(response, "로그인이 완료되었습니다.", true));
    }

    @Operation(summary = "로그인 유저 비밀번호 수정",
            security = @SecurityRequirement(name = "JWT"))
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
                                            @Parameter(hidden = true) @LoginUser AppUser loginAppUser) {
        userService.updatePassword(updatePasswordRequest, loginAppUser);
        return ResponseEntity.ok(new CommonResponse<>(null, "비밀번호 수정이 완료되었습니다.", true));
    }

    @Operation(summary = "로그인 유저 이메일 찾기",
            security = @SecurityRequirement(name = "JWT"))
    @GetMapping("/email")
    public ResponseEntity<String> findEmail(@Valid @RequestParam Long id,
                                            @Parameter(hidden = true) @LoginUser AppUser loginAppUser) {
        if (loginAppUser.getId().equals(id)) {
            String password = userService.findEmail(id);
            return ResponseEntity.ok().body(password);
        }
        throw new ForbiddenException("비밀번호 찾기 실패: 로그인한 사용자의 이메일이 아닙니다");
    }
}
