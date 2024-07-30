package gift.domain.user.controller;

import gift.auth.jwt.JwtToken;
import gift.domain.user.dto.UserRequest;
import gift.domain.user.dto.UserLoginRequest;
import gift.domain.user.service.UserService;
import gift.exception.DuplicateEmailException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "회원 API")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    public ResponseEntity<JwtToken> create(
        @Parameter(description = "회원 등록 정보", required = true)
        @RequestBody @Valid UserRequest userRequest
    ) {
        try {
            JwtToken jwtToken = userService.signUp(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException("error.duplicate.key.email");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 정보를 통해 로그인합니다.")
    public ResponseEntity<JwtToken> login(
        @Parameter(description = "회원 로그인 정보", required = true)
        @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        JwtToken jwtToken = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
