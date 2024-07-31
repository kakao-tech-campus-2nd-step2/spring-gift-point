package gift.controller;

import gift.Login;
import gift.dto.LoginMember;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderListResponse;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "상품 주문과 관련된 API Controller")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 요청 api")
    @ApiResponse(responseCode = "201", description = "주문 성공")
    public ResponseEntity<OrderResponse> order(@Login LoginMember member, @Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.order(member, orderRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회 api")
    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    public ResponseEntity<Page<OrderListResponse>> getOrderList(@Login LoginMember member, Pageable pageable) {
        return new ResponseEntity<>(orderService.getOrderList(member, pageable), HttpStatus.OK);
    }
}