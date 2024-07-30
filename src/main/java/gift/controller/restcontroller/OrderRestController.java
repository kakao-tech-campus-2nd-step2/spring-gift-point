package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.service.RedisService;
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

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final RedisService redisService;

    public OrderRestController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("")
    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @Parameter(hidden = true) @NotNull @LoginMember Long memberId
    ) {
        OrderResponse order = redisService.createOrderRedisLock(memberId, orderRequest);
        return ResponseEntity.ok().body(order);
    }
}
