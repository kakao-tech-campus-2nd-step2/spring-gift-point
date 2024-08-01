package gift.web.controller;

import gift.service.order.OrderService;
import gift.web.dto.MemberDto;
import gift.web.dto.OrderDto;
import gift.web.jwt.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@AuthUser MemberDto memberDto, @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(memberDto, orderDto), HttpStatus.CREATED);
    }
}
