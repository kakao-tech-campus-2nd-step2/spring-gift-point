package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 성공",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@LoginUser String email, @Valid @RequestBody OrderRequestDto orderRequestDto){
        OrderResponseDto orderResponseDto = orderService.addOrder(email, orderRequestDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }
}
