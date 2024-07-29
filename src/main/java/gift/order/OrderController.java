package gift.order;

import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "옵션 주문", description = "옵션을 주문하고, 메시지를 보냅니다.")
    @ApiResponse(responseCode = "201", description = "주문 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 옵션이거나, 주문한 수량이 남아있는 수량보다 작습니다.")
    public CreateOrderResponseDTO createOrder(
        @RequestBody CreateOrderRequestDTO createOrderRequestDTO,
        @RequestHeader("Authorization") String accessToken
    ) {
        return orderService.createOrder(createOrderRequestDTO, accessToken);
    }
}
