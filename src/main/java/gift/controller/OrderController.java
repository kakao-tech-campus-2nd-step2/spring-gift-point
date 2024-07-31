package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "카카오 주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 추가 성공",
            content = {@Content(schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "-40003", description = "주문 생성 시 재고보다 많은 수량을 입력함"),
        @ApiResponse(responseCode = "-40101", description = "토큰을 찾을 수 없음"),
        @ApiResponse(responseCode = "-40102", description = "토큰이 만료됨"),
        @ApiResponse(responseCode = "-40103", description = "토큰과 관련된 알 수 없는 오류"),
        @ApiResponse(responseCode = "-40404", description = "옵션을 찾을 수 없음")
    })
    public ResponseEntity<Map<String, OrderResponse>> addOrder(
        @RequestHeader("Authorization") String token,
        @RequestBody OrderRequest orderRequest) {
        String authToken = token.substring(7);

        OrderResponse response = orderService.addOrder(orderRequest, authToken);
        Map<String, OrderResponse> responseBody = new HashMap<>();
        responseBody.put("created_order", response);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "-40101", description = "토큰을 찾을 수 없음"),
        @ApiResponse(responseCode = "-40102", description = "토큰이 만료됨"),
        @ApiResponse(responseCode = "-40103", description = "토큰과 관련된 알 수 없는 오류")
    })
    public ResponseEntity<Map<String, List<OrderResponse>>> getPagedOrders(
        @RequestHeader("Authorization") String token) {

        List<OrderResponse> orders = orderService.getOrders(token);
        Map<String, List<OrderResponse>> responseBody = new HashMap<>();
        responseBody.put("orders", orders);
        return ResponseEntity.ok(responseBody);
    }

}
