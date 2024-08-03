package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.TokenAuth;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    public ResponseEntity<OrderResponse> createOrder(@LoginMember TokenAuth tokenAuth, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(tokenAuth, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/price")
    @Operation(summary = "결제 금액 조회", description = "결제 직전 페이지에서 금액을 조회한다.")
    public ResponseEntity<PriceResponse> getOrderPrice(@RequestBody PriceRequest priceRequest) {
        PriceResponse priceResponse = orderService.getOrderPrice(priceRequest);
        return ResponseEntity.ok(priceResponse);
    }

}