package gift.Controller;

import gift.DTO.OrderRequestDTO;
import gift.DTO.OrderResponseDTO;
import gift.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/product/order")
@Tag(name = "상품 주문 관련 서비스", description = " ")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "상품 주문", description = "어느 유저가 요구만큼 주문, 주문 성공시 카카오톡 메세지 송신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created order"),
            @ApiResponse(responseCode = "400", description = "Invalid order request data")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
