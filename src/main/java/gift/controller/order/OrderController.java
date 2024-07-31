package gift.controller.order;

import gift.DTO.order.OrderRequest;
import gift.DTO.order.OrderResponse;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.service.TokenService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final TokenService tokenService;

    public OrderController(
        OrderService orderService,
        KakaoService kakaoService,
        TokenService tokenService
    ) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> makeOrder(
        @RequestHeader(value = "Authorization") String authorizationHeader,
        @RequestBody @Valid OrderRequest orderRequest
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        OrderResponse orderResponse = orderService.makeOrder(token, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
