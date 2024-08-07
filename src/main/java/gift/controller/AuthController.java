package gift.controller;

import com.fasterxml.jackson.databind.JsonNode;
import gift.dto.KakaoCallbackResponseDto;
import gift.service.AuthService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
@RequestMapping("/oauth/kakao")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthController(AuthService authService, JwtUtil jwtUtil) {
    this.authService = authService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "로그인 URL 생성", description = "카카오 로그인 URL을 생성합니다.")
  @GetMapping
  public RedirectView login() {
    String authorizationUrl = authService.getAuthorizationUrl();
    logger.info("Redirecting to Kakao authorization URL: {}", authorizationUrl);
    return new RedirectView(authorizationUrl);
  }

  @Operation(summary = "카카오 로그인 콜백 처리", description = "카카오 로그인 콜백을 처리하고 JWT 토큰과 카카오 액세스 토큰을 반환합니다.")
  @GetMapping("/callback")
  public ResponseEntity<KakaoCallbackResponseDto> handleKakaoCallback(
          @RequestParam("code") String code) {
    logger.info("Received Kakao callback with code: {}", code);
    try {
      String kakaoAccessToken = authService.getToken(code);
      String jwtToken = jwtUtil.generateToken(kakaoAccessToken);
      JsonNode userInfo = authService.getKakaoUserInfo(kakaoAccessToken);
      KakaoCallbackResponseDto responseDto = new KakaoCallbackResponseDto(jwtToken, kakaoAccessToken, userInfo);
      logger.info("Returning response with JWT token and Kakao access token");
      return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    } catch (Exception e) {
      logger.error("Error handling Kakao callback", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}