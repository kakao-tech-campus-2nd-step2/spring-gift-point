package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.TokenAuth;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    public ResponseEntity<OrderResponse> createOrder(@LoginMember TokenAuth tokenAuth, @RequestBody OrderRequest orderRequest) {
        String token = tokenAuth.getToken();
        OrderResponse orderResponse = orderService.createOrder(token, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}