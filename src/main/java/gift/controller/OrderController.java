package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.TokenAuth;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.dto.response.OrderPageResponse;
import gift.service.OrderService;
import gift.util.SortUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Operation(summary = "주문하기", description = "주문을 처리한다.")
    public ResponseEntity<OrderResponse> createOrder(@Parameter(hidden = true) @LoginMember TokenAuth tokenAuth, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(tokenAuth, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/price")
    @Operation(summary = "결제 금액 조회", description = "결제 직전 페이지에서 금액을 조회한다.")
    public ResponseEntity<PriceResponse> getOrderPrice(
            @RequestParam @NotNull(message = "상품 ID를 입력하세요.") Long productId,
            @RequestParam @NotNull(message = "옵션 ID를 입력하세요.") Long optionId,
            @RequestParam
            @NotNull(message = "옵션 수량을 입력하세요.")
            @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
            @Max(value = 99999999, message = "옵션 수량은 최대 1억 개 미만이어야 합니다.") Integer quantity
    ) {
        PriceRequest priceRequest = new PriceRequest(productId, optionId, quantity);
        PriceResponse priceResponse = orderService.getOrderPrice(priceRequest);
        return ResponseEntity.ok(priceResponse);
    }

    @GetMapping
    @Operation(summary = "주문 조회 (페이지네이션 적용)", description = "회원의 모든 주문을 페이지 단위로 조회한다.")
    public ResponseEntity<OrderPageResponse> getOrders(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @Parameter(hidden = true) @LoginMember TokenAuth tokenAuth) {

        Long memberId = tokenAuth.getMemberId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtils.parseSortParameter(sort)));
        OrderPageResponse orderPageResponse = orderService.getOrdersByMemberId(memberId, pageable);
        return ResponseEntity.ok(orderPageResponse);
    }

}