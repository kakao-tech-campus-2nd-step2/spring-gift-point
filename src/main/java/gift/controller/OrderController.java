package gift.controller;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody OrderRequest orderRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, memberId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
