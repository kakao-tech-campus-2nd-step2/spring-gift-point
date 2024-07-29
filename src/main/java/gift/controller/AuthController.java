package gift.controller;

import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.service.AuthService;
import gift.service.KaKaoLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class AuthController {

    private final AuthService authService;
    private final KaKaoLoginService kaKaoLoginService;

    public AuthController(AuthService authService, KaKaoLoginService kaKaoLoginService) {
        this.authService = authService;
        this.kaKaoLoginService = kaKaoLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> memberRegister(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.addMember(authRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> memberLogin(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @GetMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return new ResponseEntity<>(kaKaoLoginService.kakaoLogin(code), HttpStatus.OK);
    }
}
