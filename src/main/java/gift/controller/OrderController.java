package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import gift.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="주문하기 API")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final TokenService tokenService;

    public OrderController(OrderService orderService, TokenService tokenService) {
        this.orderService = orderService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "회원id로 주문 생성")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request, @RequestHeader("Authorization") String token) {
        OrderResponse response;
        if (tokenService.isJwtToken(token)) {
            // JWT 토큰인 경우 응답 바디만 생성
            response = orderService.createOrder(request, token, false);
        } else {
            // 카카오 액세스 토큰인 경우 응답 바디 생성 후 메시지 전송
            response = orderService.createOrder(request, token, true);
        }
        return ResponseEntity.ok(response);
    }
}
