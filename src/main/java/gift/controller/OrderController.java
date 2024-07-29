package gift.controller;

import gift.config.LoginUser;
import gift.dto.OrderDetailResponse;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.User;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.",
        responses = @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))))
    public ResponseEntity<OrderResponse> createOrder(@Parameter(hidden = true) @LoginUser User user,
        @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.createOrder(user, request));
    }

    @GetMapping
    @Operation(summary = "사용자 모든 주문 조회", description = "사용자의 모든 주문을 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "주문 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponse.class))))
    public ResponseEntity<List<OrderDetailResponse>> getAllOrders(
        @Parameter(hidden = true) @LoginUser User user) {
        return ResponseEntity.ok().body(orderService.getAllOrders(user.getId()));
    }
}
