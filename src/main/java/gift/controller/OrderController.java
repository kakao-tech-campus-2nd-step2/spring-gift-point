package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Order;
import gift.domain.OrderRequest;
import gift.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<Order> order(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderRequest orderRequest
            ) throws IllegalAccessException, JsonProcessingException {
        return ResponseEntity.ok().body(orderService.order(token,orderRequest));
    }
}
