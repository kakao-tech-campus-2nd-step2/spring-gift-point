package gift.controller;

import static gift.util.constants.MemberConstants.INVALID_AUTHORIZATION_HEADER;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;
import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;
import static gift.util.constants.auth.TokenConstants.EXPIRED_TOKEN;
import static gift.util.constants.auth.TokenConstants.INVALID_TOKEN;

import gift.dto.orderDetail.OrderDetailRequest;
import gift.dto.orderDetail.OrderDetailResponse;
import gift.service.OrderDetailService;
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
                responseCode = "401",
                description = "유효하지 않은 Authorization 헤더 또는 토큰",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "유효하지 않은 Authorization 헤더",
                            value = "{\"error\": \"" + INVALID_AUTHORIZATION_HEADER + "\"}"
                        ),
                        @ExampleObject(name = "유효하지 않은 JWT 토큰", value = "{\"error\": \"" + INVALID_TOKEN + "\"}"),
                        @ExampleObject(name = "만료된 JWT 토큰", value = "{\"error\": \"" + EXPIRED_TOKEN + "\"}")
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "JWT 토큰으로 회원 찾기 실패",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
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
    public ResponseEntity<OrderDetailResponse> createOrder(
        @RequestBody OrderDetailRequest orderDetailRequest,
        @RequestAttribute("memberId") Long memberId
    ) {
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrder(orderDetailRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailResponse);
    }
}
