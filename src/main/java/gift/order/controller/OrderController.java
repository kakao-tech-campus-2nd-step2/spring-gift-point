package gift.order.controller;

import gift.order.model.dto.OrderRequest;
import gift.order.model.dto.OrderResponse;
import gift.order.service.OrderService;
import gift.resolver.LoginUser;
import gift.user.model.dto.AppUser;
import gift.user.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@LoginUser AppUser loginAppUser,
                                                     @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(loginAppUser, orderRequest);
        kakaoService.sendMessageToMe(loginAppUser, orderRequest.message());
        return ResponseEntity.ok().body(orderResponse);
    }
}
