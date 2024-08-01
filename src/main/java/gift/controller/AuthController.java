package gift.controller;

import gift.dto.OrderDto;
import gift.service.AuthService;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

  @Operation(summary = "토큰 콜백", description = "카카오 인증 후 콜백을 처리하여 사용자 정보를 홈페이지로 리다이렉트합니다.")
  @GetMapping("/callback")
  public RedirectView callback(@RequestParam String code) {
    String token = authService.getToken(code);
    String userInfo = authService.getKakaoUserInfo(token).toString();
    return new RedirectView("/home?userInfo=" + userInfo + "&kakaoAccessToken=" + token);
  }

  @Operation(summary = "주문 목록 조회 (페이지네이션 적용)", description = "카카오 로그인 콜백 처리하고 토큰 반환합니다.")
  @GetMapping("/callback/orders")
  public List<OrderDto> getOrderListWithPagination(@RequestHeader("Authorization") String authorization,
                                                   @RequestHeader("Kakao-Access-Token") String kakaoAccessToken,
                                                   @RequestParam int page) {
    String token = authorization.replace("Bearer ", "");
    Long memberId = jwtUtil.getMemberIdFromToken(token);

    PageRequest pageRequest = PageRequest.of(page, 10);
    return orderService.getOrders(pageRequest).getContent();
  }
}