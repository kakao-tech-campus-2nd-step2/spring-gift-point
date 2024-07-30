package gift.controller.api;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.interceptor.MemberId;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> order(@MemberId Long memberId, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.processOrder(memberId, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
