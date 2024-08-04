package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.model.CurrentMember;
import gift.model.Member;
import gift.model.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order", description = "상품 주문 관련 api")
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PostMapping
    @Operation(summary = "주문하기", description = "주문을 합니다.")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                                        @CurrentMember Member member) {
        Order order = orderService.createOrder(orderRequestDto, member.getId());
        OrderResponseDto orderResponseDto = new OrderResponseDto(order);
        return ResponseEntity.status(201).body(orderResponseDto);
    }

    @GetMapping
    @Operation(summary = "모든 주문 조회", description = "모든 주문을 조회합니다.")
    public ResponseEntity<Page<Order>> getAllOrders(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Order> orderPage = orderService.getOrders(pageable);
        return ResponseEntity.ok(orderPage);
    }
}
