package gift.controller;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "주문 관리 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "주문 성공"),
            @ApiResponse(
                responseCode = "400",
                description = "옵션 수량 부족",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INSUFFICIENT_QUANTITY + "(옵션 Id)\"}")
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 옵션 Id",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + OPTION_NOT_FOUND + "(옵션 Id)\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @RequestBody OrderRequest orderRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
