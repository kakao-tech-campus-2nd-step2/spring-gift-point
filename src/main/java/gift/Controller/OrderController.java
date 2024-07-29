package gift.Controller;

import gift.Model.OrderRequestDTO;
import gift.Model.OrderResponseDTO;

import gift.Service.OrderService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order 관련 API")
@RestController
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @Operation(
        summary = "상품 주문하기",
        description = "정보를 전달받아 상품을 주문"
    )
    @ApiResponse(
        responseCode = "201",
        description = "상품 주문하기 성공"
    )
    @Parameters({
        @Parameter(name = "jwtToken", description = "회원 및 다양한 정보를 얻기 위해 클라이언트와 주고 받는 jwtToken"),
        @Parameter(name = "orderRequestDTO", description = "주문하기 정보를 담은 객체")
    })
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponseDTO> doOrder(@RequestHeader(value = "Authorization") String jwtToken, @RequestBody OrderRequestDTO orderRequestDTO) {
        // 주문하기 후 response
        OrderResponseDTO response = orderService.doOrder(jwtToken, orderRequestDTO);
        // 메세지 보내기
        orderService.sendMessage(orderRequestDTO.getMessage(), jwtToken);
        return ResponseEntity.status(201).body(response); // 201코드와 response 리턴
    }
}
