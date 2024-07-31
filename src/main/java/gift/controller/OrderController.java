package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.OrderRequest;
import gift.dto.response.GetOrdersResponse;
import gift.dto.response.OrderResponse;
import gift.exception.CustomException;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "order", description = "Order API")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil){
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @Operation(summary = "주문조회", description = "주문을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 조회 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰"),
    })
    public ResponseEntity<GetOrdersResponse> getOrder(@RequestHeader("Authorization") String authorizationHeader){
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }
        return new ResponseEntity<>(orderService.getOrders(jwtUtil.extractToken(authorizationHeader)), HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "주문하기", description = "파라미터로 받은 주문을 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 성공"),
        @ApiResponse(responseCode = "400", description = "상품의 옵션은 최소 1개는 존재해야함"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<OrderResponse> order(@RequestHeader("Authorization") String authorizationHeader, @RequestBody OrderRequest orderRequest){
        
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }

        return new ResponseEntity<>(orderService.makeOrder(jwtUtil.extractToken(authorizationHeader), orderRequest), HttpStatus.CREATED);
    }
}
