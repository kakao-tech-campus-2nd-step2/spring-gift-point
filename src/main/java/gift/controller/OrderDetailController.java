package gift.controller;

import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;

import gift.config.CommonApiResponses.CommonForbiddenResponse;
import gift.config.CommonApiResponses.CommonServerErrorResponse;
import gift.config.CommonApiResponses.CommonUnauthorizedResponse;
import gift.dto.orderDetail.OrderDetailRequest;
import gift.dto.orderDetail.OrderDetailResponse;
import gift.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @Operation(summary = "(명세 통일) 주문 생성", description = "새로운 주문을 생성합니다.")
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
            )
        }
    )
    @CommonUnauthorizedResponse
    @CommonForbiddenResponse
    @CommonServerErrorResponse
    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrder(
        @RequestBody OrderDetailRequest orderDetailRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrder(orderDetailRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailResponse);
    }
}
