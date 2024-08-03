package gift.controller;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;
import static gift.util.constants.MemberConstants.INSUFFICIENT_POINTS;
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
                description = "올바르지 않은 요청",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "옵션 수량 부족",
                            value = "{\"error\": \"" + INSUFFICIENT_QUANTITY + "(옵션 Id)\"}"
                        ),
                        @ExampleObject(
                            name = "포인트 부족",
                            value = "{\"(필드 명)\": \"" + INSUFFICIENT_POINTS + "(잔여 포인트)\"}"
                        ),
                        @ExampleObject(name = "필수 입력 값 누락", value = "{\"error\": \"" + REQUIRED_FIELD_MISSING + "\"}")
                    }
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
