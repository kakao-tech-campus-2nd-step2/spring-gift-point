package gift.controller;

import gift.dto.OrderRequest;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        Long memberId = (Long) request.getAttribute("memberId");

        orderService.createOrder(orderRequest, memberId);
        return ResponseEntity.ok().build();
    }
}
