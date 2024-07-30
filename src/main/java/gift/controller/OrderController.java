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
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 주문 목록을 페이지별로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(schema = @Schema(implementation = PagedModel.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> orderList(@Parameter(hidden = true) @LoginMember Member member,

                                                         @Parameter(description = "쿼리 파라미터: page, size, sort 지정")
                                                         @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<OrderDto> dtos = orderService.getOrders(member, pageable);

        List<OrderResponse> orderResponses = dtos.stream()
                .map(OrderDto::toResponseDto)
                .toList();

        PageImpl<OrderResponse> response = new PageImpl<>(orderResponses, pageable, orderResponses.size());

        return ResponseEntity.ok()
                .body(response);
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
