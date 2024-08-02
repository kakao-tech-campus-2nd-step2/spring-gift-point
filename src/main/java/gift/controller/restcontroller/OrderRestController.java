package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.controller.dto.response.PagingResponse;
import gift.service.OrderService;
import gift.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final RedisService redisService;
    private final OrderService orderService;

    public OrderRestController(RedisService redisService,OrderService orderService) {
        this.redisService = redisService;
        this.orderService = orderService;
    }

    @PostMapping("")
    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Void> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @Parameter(hidden = true) @NotNull @LoginMember Long memberId
    ) {
        redisService.createOrderRedisLock(memberId, orderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    @Operation(summary = "주문 목록 조회", description = "주문목록을 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<PagingResponse<OrderResponse>> getOrders(
            @Parameter(hidden = true) @NotNull @LoginMember Long memberId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PagingResponse<OrderResponse> pages = orderService.findOrdersByMemberId(memberId, pageable);
        return ResponseEntity.ok().body(pages);
    }

}
