package gift.api;

import gift.dto.OrderDTO;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    @Operation(summary = "Create order", description = "This API creates a new order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created and message sent."),
        @ApiResponse(responseCode = "400", description = "Invalid product quantity."),
        @ApiResponse(responseCode = "500", description = "Order created but failed to send message.")
    })
    public ResponseEntity<OrderResponse> createOrder(
        @RequestHeader("Authorization") String authorization,
        @RequestBody OrderDTO orderDTO) {


        OrderResponse orderResponse = orderService.createOrder(authorization, orderDTO);
        return ResponseEntity.ok(orderResponse);
    }
}