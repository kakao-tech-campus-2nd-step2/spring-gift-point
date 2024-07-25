package gift.order.controller;

import gift.order.model.dto.OrderRequest;
import gift.order.model.dto.OrderResponse;
import gift.order.service.OrderService;
import gift.resolver.LoginUser;
import gift.user.model.dto.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@LoginUser AppUser loginAppUser,
                                                     @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(loginAppUser, orderRequest);

        return ResponseEntity.ok().body(orderResponse);
    }
}
