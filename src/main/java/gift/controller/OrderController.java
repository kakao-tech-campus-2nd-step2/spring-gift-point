package gift.controller;

import gift.model.AuthInfo;
import gift.model.OrderRequest;
import gift.model.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "상품 옵션의 수량을 감소시키는 주문(order) 카카오톡 메시지를 나에게 전송합니다.",
        parameters = {
            @Parameter(
                name = "Authorization",
                description = "카카오톡 로그인 인증 정보(Access Token)",
                required = true,
                in = ParameterIn.HEADER,
                schema = @Schema(type = "string"),
                example = "Bearer some_access_token"
            )
        }
    )

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "정상적으로 주문 카카오톡 메시지가 전송됩니다.",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = OrderRequest.class))}),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.", content = @Content)}
    )
    public ResponseEntity<?> sendOrderMessage(
        @Parameter(description = "주문 메시지에 대한 내용", required = true)
        @RequestBody OrderRequest request,
        AuthInfo authInfo) {
        OrderResponse response = orderService.createOrder(request);
        orderService.sendOrderMessage(request, authInfo.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
