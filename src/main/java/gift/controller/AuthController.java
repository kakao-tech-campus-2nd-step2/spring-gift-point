package gift.controller;

import gift.dto.KakaoCallbackResponseDto;
import gift.dto.OrderDto;
import gift.service.AuthService;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
@RequestMapping("/oauth/kakao")
public class AuthController {

  private final AuthService authService;
  private final OrderService orderService;
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthController(AuthService authService, OrderService orderService, JwtUtil jwtUtil) {
    this.authService = authService;
    this.orderService = orderService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "로그인 URL 생성", description = "카카오 로그인 URL을 생성합니다.")
  @GetMapping("/login")
  public RedirectView login() {
    String authorizationUrl = authService.getAuthorizationUrl();
    return new RedirectView(authorizationUrl);
  }

  @Operation(summary = "카카오 로그인 콜백 처리", description = "카카오 로그인 콜백을 처리하고 JWT 토큰과 카카오 액세스 토큰을 반환합니다.")
  @GetMapping("/callback")
  public ResponseEntity<KakaoCallbackResponseDto> handleKakaoCallback(
          @RequestParam("jwtToken") String jwtToken,
          @RequestParam("kakaoAccessToken") String kakaoAccessToken) {
    KakaoCallbackResponseDto responseDto = new KakaoCallbackResponseDto(jwtToken, kakaoAccessToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

}