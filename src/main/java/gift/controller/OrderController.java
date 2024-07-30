package gift.controller;

import gift.dto.OrderDTO;
import gift.security.JwtTokenProvider;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class.getName());

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            LOGGER.warning("Authorization header is missing or does not start with Bearer");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        token = token.substring(7); // "Bearer " 제거
        String userEmail = jwtTokenProvider.getUsernameFromToken(token);

        if (userEmail == null) {
            LOGGER.warning("Invalid JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        LOGGER.info("User from token: " + userEmail);
        OrderDTO createdOrder = orderService.createOrder(orderDTO, userEmail);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/order")
    public String showOrderPage() {
        return "order";  // templates/order.html 템플릿을 반환
    }
}