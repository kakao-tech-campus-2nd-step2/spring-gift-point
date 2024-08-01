package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.dto.OrderAddRequestDto;
import gift.domain.model.dto.OrderResponseDto;
import gift.domain.model.entity.User;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Validated
@Tag(name = "Order", description = "주문 관리 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 추가", description = "새로운 주문을 추가합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@LoginUser User user,
        @Valid @RequestBody OrderAddRequestDto orderAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.addOrder(user, orderAddRequestDto));
    }
}
