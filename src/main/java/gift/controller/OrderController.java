package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.User.UserResponse;
import gift.security.AuthenticateMember;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /*
     * 주문하기 ( 주문 추가 )
     */
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> order(
            @RequestBody OrderRequest orderRequest,
            @RequestParam Long productId,
            @AuthenticateMember UserResponse user
    ) throws JsonProcessingException {
        OrderResponse order = orderService.order(orderRequest, user, productId);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
