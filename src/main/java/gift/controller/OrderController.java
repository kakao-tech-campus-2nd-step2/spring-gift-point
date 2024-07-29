package gift.controller;

import gift.config.LoginMember;
import gift.domain.member.Member;
import gift.dto.OrderDto;
import gift.dto.request.OrderCreateRequest;
import gift.dto.response.ErrorResponse;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Order API", description = "주문 관련 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "상품 주문", description = "사용자가 상품을 주문합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 성공", content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "옵션 또는 상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<OrderResponse> order(@Parameter(hidden = true) @LoginMember Member member,
                                               @RequestBody @Valid OrderCreateRequest request) {
        OrderDto dto = OrderDto.of(member, request);
        OrderDto orderDto = orderService.processOrder(dto);
        OrderResponse response = orderDto.toResponseDto();

        return ResponseEntity.status(CREATED)
                .body(response);
    }

}
