package gift.auth;

import gift.exception.NotFoundMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "카카오 로그인 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 API", description = "토큰을 가지고 기존 회원인지 판단 후 로그인")
    public String login(@RequestHeader("Authorization") String authHeader, @RequestBody LoginRequestDto loginRequestDto)
        throws NotFoundMember {
        return authService.login(authHeader,loginRequestDto);
    }

    @GetMapping("/token")
    @Operation(summary = "토큰 발급 API", description = "인가 코드를 가지고 토큰 발급")
    public String getToken(@RequestParam("code") String authorizationCode) {
        return authService.getTokenFromAuthorizationCode(authorizationCode);
    }

}
