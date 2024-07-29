package gift.controller.order;

import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.service.AuthService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order", description = "Order API")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "create Order", description = "주문 생성")
    public void createOrder(@LoginUser LoginResponse member, @RequestBody OrderRequest order) {
        ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.save(member.id(), order));
    }
}