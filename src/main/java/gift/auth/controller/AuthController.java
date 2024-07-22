package gift.auth.controller;

import gift.auth.dto.LoginRequestDto;
import gift.auth.service.AuthService;
import gift.global.response.ResultCode;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<SimpleResultResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Map<String, String> headers = authService.login(loginRequestDto);
        return ResponseHelper.createSimpleResponse(ResultCode.LOGIN_SUCCESS, headers);
    }
}
