package gift.controller;

import gift.model.order.OrderRequest;
import gift.model.order.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문하기 API", description = "주문 기능 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성 기능", description = "새로운 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestHeader("Authorization") String authorizationCode,
        @RequestBody OrderRequest orderRequest) {
        String token = authorizationCode.replace("Bearer ", "");
        OrderResponse order = orderService.createOrder(token, orderRequest);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(summary = "주문 삭제 기능", description = "id를 받아서 주문을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
