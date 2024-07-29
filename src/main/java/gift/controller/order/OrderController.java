package gift.controller.order;

import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import gift.controller.order.dto.OrderRequest;
import gift.controller.order.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    @Operation(summary = "주문 등록", description = "주문을 등록합니다.")
    public ResponseEntity<OrderResponse> order(@LoginUser LoginInfo user, @RequestBody OrderRequest request) {
        OrderResponse response = orderService.order(user.id(), request);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + response.id())).body(response);
    }
}
