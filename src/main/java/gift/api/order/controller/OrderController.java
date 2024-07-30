package gift.api.order.controller;

import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.service.OrderFacade;
import gift.global.resolver.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    @Operation(summary = "주문하기")
    public ResponseEntity<OrderResponse> order(@LoginMember Long memberId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderFacade.order(memberId, orderRequest);
        return ResponseEntity.created(URI.create("/api/orders")).body(orderResponse);
    }
}
