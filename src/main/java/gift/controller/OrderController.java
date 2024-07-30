package gift.controller;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.Order;
import gift.service.KakaoAuthService;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            String accessToken = token.substring(7); // "Bearer " 제거
            String email = kakaoAuthService.getEmailFromAccessToken(accessToken);
            Order order = orderService.placeOrder(email, orderRequestDTO, accessToken);
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
