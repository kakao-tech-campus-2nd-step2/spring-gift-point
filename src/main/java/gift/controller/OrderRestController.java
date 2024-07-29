package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "새로운 주문을 생성합니다")
    @PostMapping
    public ResponseEntity<Void> order(@RequestHeader(HttpHeaders.AUTHORIZATION) String header, @LoginMember MemberRequest memberRequest, @RequestBody OrderRequest orderRequest){
        String token = header.substring(7);
        orderService.orderOption(orderRequest, memberRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
