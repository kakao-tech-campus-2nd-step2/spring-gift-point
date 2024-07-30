package gift.controller.api;

import gift.dto.auth.AuthResponse;
import gift.dto.auth.LoginRequest;
import gift.dto.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API")
public interface AuthApi {

    @Operation(summary = "새로 회원 가입을 진행하고 토큰을 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "409", description = "회원 가입 실패(사유 : 이미 존재하는 이메일입니다. )"),
    })
    ResponseEntity<AuthResponse> register(RegisterRequest registerRequest);

    @Operation(summary = "로그인을 하고 토큰을 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "회원 등록 실패(사유 : 이메일이나 비밀번호가 올바르지 않습니다. )"),
    })
    ResponseEntity<AuthResponse> login(LoginRequest loginRequest);

    @Operation(summary = "카카오 로그인을 통해 회원을 인증하고 토큰을 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "회원 등록 실패(사유 : 이미 가입된 이메일입니다. )"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류"),
    })
    ResponseEntity<Void> redirectKakaoLogin();
}
