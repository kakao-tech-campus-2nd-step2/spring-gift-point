package gift.domain.order.controller;

import gift.config.LoginUser;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import gift.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "주문하기 API")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문 정보를 생성합니다.")
    public ResponseEntity<OrderResponse> create(
        @Parameter(description = "주문 요청 정보", required = true)
        @RequestBody @Valid OrderRequest orderRequest,
        @Parameter(hidden = true)
        @LoginUser User user
    ) {
        OrderResponse orderResponse = orderService.createAndSendMessage(orderRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
