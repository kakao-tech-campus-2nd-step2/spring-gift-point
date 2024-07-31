package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.ErrorResponse;
import gift.dto.OrderRequestDTO;
import gift.entity.Orders;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order(상품 주문)", description = "상품 주문 관련 API입니다.")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 주문 목록을 페이지별로 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PagedModel.class)))),
        @ApiResponse(responseCode = "401", description = "유효한 토큰 필요", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    @GetMapping
    public ResponseEntity<Page<Orders>> getOrderList(
        @LoginUser String email,
        @ParameterObject @PageableDefault(page=0, size=10, sort="id") Pageable pageable) {
        Page<Orders> order = orderService.getProductPage(email, pageable);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(summary = "상품 주문", description = "사용자가 상품을 주문합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 주문 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "401", description = "유효한 토큰 필요.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품, 옵션 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    @PostMapping
    public ResponseEntity<String> addOrder(@Valid @RequestBody OrderRequestDTO orderRequest,
        @LoginUser String email) {
        orderService.orderOption(orderRequest, email);
        return new ResponseEntity<>("상품 주문 완료", HttpStatus.CREATED);
    }


}
