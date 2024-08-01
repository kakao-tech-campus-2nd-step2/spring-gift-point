package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Order.Order;
import gift.domain.Order.OrderRequest;
import gift.service.JwtService;
import gift.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    private JwtService jwtService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<Order> order(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderRequest orderRequest
            ) throws IllegalAccessException, JsonProcessingException, AuthenticationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.order(token,orderRequest));
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAllOrder(
            Pageable pageable
    ){
        String jwtId = jwtService.getMemberId();
        return ResponseEntity.ok().body(orderService.findAll(jwtId,pageable));
    }
}
