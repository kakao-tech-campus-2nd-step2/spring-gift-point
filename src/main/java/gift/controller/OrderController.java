package gift.controller;

import gift.domain.AppUser;
import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {
    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "로그인한 사용자의 주문 생성")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@LoginUser AppUser loginAppUser,
                                                     @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(loginAppUser, orderRequest);
        kakaoService.sendMessageToMe(loginAppUser, orderRequest.message());
        return ResponseEntity.ok().body(orderResponse);
    }
}
