package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.service.OrderService;
import gift.service.RedisService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Orde API")
@RestController
@RequestMapping("/api/v1/order")
public class OrderRestController {

    private final OrderService orderService;
    private final RedisService redisService;
    private final WishService wishService;

    public OrderRestController(OrderService orderService, RedisService redisService, WishService wishService) {
        this.orderService = orderService;
        this.redisService = redisService;
        this.wishService = wishService;
    }

    @PostMapping("")
    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @Parameter(hidden = true) @NotNull @LoginMember Long memberId
    ) {
        OrderResponse order = redisService.createOrderRedisLock(memberId, orderRequest);
        wishService.deleteIfExists(orderRequest.productId(), memberId);
        orderService.sendKakaoMessage(memberId, order.orderId());
        return ResponseEntity.ok().body(order);
    }
}
