package gift.product.presentation.restcontroller;

import gift.global.authentication.annotation.MemberId;
import gift.product.business.service.OrderService;
import gift.product.presentation.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/options")
    public ResponseEntity<Long> createOrder(
        @MemberId Long memberId,
        @RequestBody @Valid OrderRequest.Create orderRequestCreate) {
        var orderInCreate = orderRequestCreate.toOrderInCreate(memberId);
        var orderId = orderService.createOrder(orderInCreate);
        return ResponseEntity.ok(orderId);
    }

}
