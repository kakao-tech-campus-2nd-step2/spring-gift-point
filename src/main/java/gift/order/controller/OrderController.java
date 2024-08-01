package gift.order.controller;

import gift.order.domain.CreateOrderRequest;
import gift.order.domain.OrderCreateResponse;
import gift.order.service.OrderService;
import gift.util.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OrderController", description = "주문 관련 API")
@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
//    @PostMapping("/orders")
//    public ResponseEntity<?> createOrder(
//            @RequestBody CreateOrderRequest createOrderRequest,
//            @Parameter(description = "액세스 토큰") @RequestHeader String accessToken) {
//        OrderCreateResponse order = orderService.createOrder(createOrderRequest, accessToken);
//        return ResponseEntity.ok().body(new CommonResponse<>(
//                order,
//                "주문 생성 성공",
//                true
//        ));
//    }
}
