package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order Management", description = "Order Management API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/api/orders")
    @Operation(summary = "새로운 주문 요청", description = "새로운 주문을 요청할 때 사용하는 API")
    public ResponseEntity<OrderResponseDto> requestOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                         @RequestHeader("Authorization") String accessToken) {
        OrderResponseDto orderResponseDto = orderService.requestOrder(orderRequestDto,accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }
}
