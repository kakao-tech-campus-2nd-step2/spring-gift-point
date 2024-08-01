package gift.api.order.controller;

import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.order.service.OrderFacade;
import gift.api.order.service.OrderService;
import gift.global.ListResponse;
import gift.global.resolver.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<ListResponse<OrderResponse>> getOrders(
        @Parameter(name = "Authorization", required = true, description = "사용자 액세스 토큰")
        @LoginMember Long memberId) {

        return ResponseEntity.ok().body(ListResponse.of(orderService.getOptions(memberId)));
    }

    @PostMapping
    @Operation(summary = "주문하기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
    public ResponseEntity<OrderResponse> order(
        @Parameter(name = "Authorization", required = true, description = "사용자 액세스 토큰")
        @LoginMember Long memberId,
        @Parameter(required = true, description = "주문 요청 본문")
        @RequestBody OrderRequest orderRequest) {

        OrderResponse orderResponse = orderFacade.order(memberId, orderRequest);
        return ResponseEntity.created(URI.create("/api/orders")).body(orderResponse);
    }
}
