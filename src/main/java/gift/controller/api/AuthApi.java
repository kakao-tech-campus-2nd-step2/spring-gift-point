package gift.controller.api;

import gift.dto.auth.AuthResponse;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API")
public interface AuthApi {

    @Operation(summary = "새 회원을 등록하고 토큰을 받는다.")
    ResponseEntity<AuthResponse> register(RegisterRequest registerRequest);

    @Operation(summary = "회원을 인증하고 토큰을 받는다.")
    ResponseEntity<AuthResponse> login(LoginRequest loginRequest);

    @Operation(summary = "카카오 로그인을 통해 회원을 인증하고 토큰을 받는다.")
    ResponseEntity<Void> redirectKakaoLogin();
}
