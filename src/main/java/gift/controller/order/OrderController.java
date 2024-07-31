package gift.controller.order;

import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import gift.common.dto.PageResponse;
import gift.controller.order.dto.OrderRequest;
import gift.controller.order.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    @Operation(summary = "주문 등록", description = "주문을 등록합니다.")
    public ResponseEntity<Void> order(@LoginUser LoginInfo user, @RequestBody OrderRequest request) {
        OrderResponse.Info response = orderService.order(user.id(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<OrderResponse.Info>> getOrderList(
        @LoginUser LoginInfo user,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
        ) {
        PageResponse<OrderResponse.Info> response = orderService.getAllOrders(user.id(), pageable);
        return ResponseEntity.ok().body(response);
    }
}
