package gift.controller;

import gift.dto.user.UserDTO;
import gift.jwtutil.JwtUtil;
import gift.service.AuthService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Validated
@Controller
@RequestMapping("/api/members/register")
public class RegisterController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public RegisterController(AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @Operation(summary = "회원가입",
            description = "새로운 사용자를 등록합니다.",
            responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 값 입력"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<Void> registUser(@Valid @RequestBody UserDTO userDTO) {
        authService.redundantUser("regist", userDTO);
        userService.createUser(userDTO);

        var responseEntity = authService.createResponse(jwtUtil.makeToken(userDTO));

        return responseEntity;
    }

}

