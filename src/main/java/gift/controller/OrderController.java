package gift.controller;

import gift.anotation.LoginMember;
import gift.domain.Member;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@LoginMember Member member, @RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO orderResponse = orderService.createOrder(member, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}