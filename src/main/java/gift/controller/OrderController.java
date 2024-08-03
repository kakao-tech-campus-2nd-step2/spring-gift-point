package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.dto.OrderAddRequestDto;
import gift.domain.model.dto.OrderResponseDto;
import gift.domain.model.entity.User;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
        @RequestParam(defaultValue = "10") @Positive int size,
        @RequestParam(defaultValue = "createdDate,desc") String sort,
        @LoginUser User user) {
        return ResponseEntity.ok(orderService.getOrders(page, size, sort, user));
    }

    @Operation(summary = "주문 추가", description = "새로운 주문을 추가합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@LoginUser User user,
        @Valid @RequestBody OrderAddRequestDto orderAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.addOrder(user, orderAddRequestDto));
    }
}
