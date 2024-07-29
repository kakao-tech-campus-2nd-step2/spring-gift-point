package gift.domain.order.controller;

import gift.annotation.LoginMember;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@Tag(name = "OrderController", description = "Order API(JWT 인증 필요)")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @Operation(summary = "주문 하기", description = "상품을 주문합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<OrderResponse> createOrder(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(member, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }
}
