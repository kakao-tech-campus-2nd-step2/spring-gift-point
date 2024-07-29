package gift.controller.order;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.model.user.User;
import gift.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderSpecification {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{giftId}")
    public ResponseEntity<OrderResponse> order(@RequestAttribute("user") User user,
                                               @PathVariable Long giftId,
                                               @Valid @RequestBody OrderRequest.Create orderRequest) {
        OrderResponse orderResponse = orderService.order(user.getId(), giftId, orderRequest);
        orderService.sendMessage(orderRequest, user, giftId);
        return ResponseEntity.ok(orderResponse);
    }
}