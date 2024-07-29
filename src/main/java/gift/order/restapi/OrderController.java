package gift.order.restapi;

import gift.advice.ErrorResponse;
import gift.advice.GatewayToken;
import gift.advice.LoggedInUser;
import gift.core.domain.order.Order;
import gift.core.domain.order.OrderService;
import gift.order.restapi.dto.OrderRequest;
import gift.order.restapi.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "주문")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    @Operation(
            summary = "상품 주문",
            description = "상품을 주문합니다.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "X-GATEWAY-TOKEN",
                            required = true,
                            schema = @Schema(type = "string"),
                            description = "카카오톡 메시지를 보내기 위한 액세스 토큰입니다."
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품을 주문합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "주문 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public OrderResponse orderProduct(
            @LoggedInUser Long userId,
            @GatewayToken String gatewayAccessToken,
            @RequestBody OrderRequest request
    ) {
        Order order = orderService.orderProduct(request.toOrder(userId), gatewayAccessToken);
        return OrderResponse.of(order);
    }
}
