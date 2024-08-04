package gift.controller;

import gift.CustomAnnotation.RequestRole;
import gift.model.entity.Role;
import gift.model.form.OrderForm;
import gift.model.response.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Api(카카오 로그인 필요)")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestRole(Role.ROLE_USER)
    @Operation(summary = "상품 주문", description = "특정 상품을 주문합니다.")
    @PostMapping
    public ResponseEntity<?> handleOrderToMe(@RequestBody OrderForm form,
        @RequestAttribute("userId") Long userId) {
        OrderResponse orderResponse = new OrderResponse(orderService.executeOrder(userId, form));
        return ResponseEntity.ok(orderResponse);
    }
}
