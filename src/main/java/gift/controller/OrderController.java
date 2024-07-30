package gift.controller;

import gift.dto.OrderDTO;
import gift.dto.OrderRequest;
import gift.service.OrderService;
import gift.util.LoginMember;
import gift.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "주문 API", description = "주문 관련된 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@LoginMember Member member, @RequestBody OrderRequest orderRequest) {
        OrderDTO orderDTO = orderService.createOrder(member.getId(), orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage());
        return ResponseEntity.status(201).body(orderDTO);
    }
}
