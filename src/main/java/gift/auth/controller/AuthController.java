package gift.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.dto.LoginRequestDto;
import gift.auth.service.AuthService;
import gift.global.response.ResultCode;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
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
        HttpHeaders headers = authService.login(loginRequestDto);
        return ResponseHelper.createSimpleResponse(ResultCode.LOGIN_SUCCESS, headers);
    }

    @GetMapping("/kakao")
    public RedirectView kakaoAuth() {
        return new RedirectView(authService.getKakaoAuthUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        HttpHeaders headers = authService.kakaoLogin(code);
        return ResponseEntity.status(200)
                .headers(headers)
                .build();
    }
}
