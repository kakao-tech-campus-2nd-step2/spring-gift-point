package gift.controller.restcontroller;

import gift.controller.dto.request.SignInRequest;
import gift.controller.dto.response.TokenResponse;
import gift.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "로그인 API")
@RestController
@RequestMapping("/api/members")
public class AuthRestController {
    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 시도합니다.")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody SignInRequest request) {
        TokenResponse response = authService.signIn(request);
        return ResponseEntity.ok().body(response);
    }
}
