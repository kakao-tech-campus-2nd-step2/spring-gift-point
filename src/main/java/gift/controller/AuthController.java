package gift.controller;

import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.dto.responsedto.UserResponseDTO;
import gift.service.AuthService;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessBody<UserResponseDTO>> signUp(
        @Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        userService.join(userSignupRequestDTO);
        UserResponseDTO userResponseDTO = authService.register(userSignupRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "회원가입에 성공했습니다.", userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessBody<UserResponseDTO>> login(
        @Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        userService.findByEmail(userLoginRequestDTO);
        UserResponseDTO userResponseDTO = authService.login(userLoginRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.ACCEPTED, "로그인에 성공했습니다.", userResponseDTO);
    }
}
