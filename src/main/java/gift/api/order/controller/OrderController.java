package gift.api.order.controller;

import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.service.OrderFacade;
import gift.api.order.service.OrderService;
import gift.global.ListResponse;
import gift.global.resolver.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    public OrderController(OrderFacade orderFacade, OrderService orderService) {
        this.orderFacade = orderFacade;
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "주문 조회", description = "사용자별 주문 목록 조회")
    public ResponseEntity<ListResponse<OrderResponse>> getOrders(@LoginMember Long memberId) {
        return ResponseEntity.ok().body(ListResponse.of(orderService.getOptions(memberId)));
    }

    @PostMapping
    @Operation(summary = "주문하기")
    public ResponseEntity<OrderResponse> order(@LoginMember Long memberId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderFacade.order(memberId, orderRequest);
        return ResponseEntity.created(URI.create("/api/orders")).body(orderResponse);
    }
}
