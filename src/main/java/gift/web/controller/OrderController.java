package gift.web.controller;

import gift.service.order.OrderService;
import gift.web.dto.MemberDto;
import gift.web.dto.order.OrderRequestDto;
import gift.web.dto.order.OrderResponseDto;
import gift.web.jwt.AuthUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<OrderResponseDto> createOrder(@AuthUser MemberDto memberDto, @RequestBody @Valid OrderRequestDto orderRequestDto) {
        return new ResponseEntity<>(orderService.createOrder(memberDto, orderRequestDto), HttpStatus.CREATED);
    }
}
