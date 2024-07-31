package gift.controller;

import gift.dto.OrderDTO;
import gift.service.JwtUtil;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "상품 주문 API")
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
    @Operation(summary = "상품 주문", description = "상품 주문 시 카카오톡 메시지가 발송됩니다.")
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token, @RequestBody OrderDTO order) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(parsedToken);
        String email = jwtUtil.extractEmail(parsedToken);
        String access_token = jwtUtil.extractKakaoToken(parsedToken);

        orderService.createOrder(order, userId, email, access_token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
