package gift.domain.order.controller;

import gift.annotation.LoginMember;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping()
    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.(Page 내부 값은 OrderResponse 입니다.)", content = @Content(schema = @Schema(implementation = Page.class), mediaType = "application/json"))
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
        @Parameter(hidden = true) @LoginMember Member member,
        @ParameterObject Pageable pageable
    ) {
        Page<OrderResponse> responses = orderService.getAllOrders(pageable, member);
        return ResponseEntity.ok(responses);
    }

    @PostMapping()
    @Operation(summary = "주문 하기", description = "상품을 주문합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "text/plain;charset=UTF-8\n")),
        @ApiResponse(responseCode = "403", description = "인가 실패", content = @Content(mediaType = "text/plain;charset=UTF-8\n")),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "text/plain;charset=UTF-8\n"))
    })
    public ResponseEntity<OrderResponse> createOrder(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(member, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }
}
