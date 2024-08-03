package gift.controller;


import gift.dto.user.UserDTO;
import gift.service.AuthService;
import gift.jwtutil.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@Controller
@RequestMapping("api/members/login")
public class LoginController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public LoginController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @Operation(summary = "로그인",
            description = "사용자가 이메일과 비밀번호를 사용하여 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 값 입력 (이메일 형식 틀림, null 값)"),
                    @ApiResponse(responseCode = "403", description = "로그인 실패")
            })
    public ResponseEntity<Void> loginUser(@Valid @RequestBody UserDTO userDTO) {
        authService.redundantUser("login", userDTO);
        authService.comparePassword(userDTO);

        var responseEntity = authService.createResponse(jwtUtil.makeToken(userDTO));

        return responseEntity;
    }
}
