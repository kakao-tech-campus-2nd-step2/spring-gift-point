package gift.controller;

import gift.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "로그인 URL 생성", description = "카카오 로그인 URL을 생성합니다.")
  @GetMapping("/login")
  public String login() {
    String authorizationUrl = authService.getAuthorizationUrl();
    return authorizationUrl;
  }

  @Operation(summary = "토큰 콜백", description = "카카오 인증 후 콜백을 처리하여 토큰을 반환합니다.")
  @GetMapping("/oauth/callback/kakao")
  public String callback(@RequestParam String code) {
    String token = authService.getToken(code);
    return token;
  }
}