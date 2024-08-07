package gift.controller;

import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Account", description = "회원가입, 로그인 등 사용자 계정과 관련된 API Controller")
public class AuthController {

    @Value("${kakao.url.code}")
    private String getCodeUrl;
    private final AuthService authService;

    private String baseUrl;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "이메일 회원가입 api")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    public ResponseEntity<AuthResponse> memberRegister(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.addMember(authRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 api")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    public ResponseEntity<AuthResponse> memberLogin(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오 회원가입 및 로그인 api")
    @ApiResponse(responseCode = "200", description = "카카오 로그인 성공")
    public RedirectView kakaoLoginRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(getCodeUrl);
        return redirectView;
    }

    @Hidden
    @GetMapping("/kakao/redirect")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return new ResponseEntity<>(authService.kakaoLogin(code), HttpStatus.OK);
    }

}
