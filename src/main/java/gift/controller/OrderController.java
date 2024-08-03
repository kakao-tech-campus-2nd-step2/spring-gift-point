package gift.controller;

import gift.dto.OrderRequest;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @Autowired
    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        Claims claims = jwtUtil.extractClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());

        orderService.createOrder(orderRequest, memberId);
        return ResponseEntity.ok().build();
    }
}
