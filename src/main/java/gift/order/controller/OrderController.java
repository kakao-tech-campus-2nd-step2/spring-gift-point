package gift.order.controller;

import gift.member.presentation.request.ResolvedMember;
import gift.order.application.OrderService;
import gift.order.application.response.OrderSaveServiceResponse;
import gift.order.controller.request.OrderCreateRequest;
import gift.order.controller.response.OrderCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderApi {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponse> order(
            @RequestBody OrderCreateRequest request,
            ResolvedMember resolvedMember
    ) {
        OrderSaveServiceResponse order = orderService.save(request.toCommand(), resolvedMember.id());
        return ResponseEntity.status(CREATED)
                .body(OrderCreateResponse.of(
                        order.id(),
                        request.optionId(),
                        request.quantity(),
                        request.message(),
                        order.orderDateTime(),
                        order.originalPrice(),
                        order.finalPrice())
                );
    }
}
