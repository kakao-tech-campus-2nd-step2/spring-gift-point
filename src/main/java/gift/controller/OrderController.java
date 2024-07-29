package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader("Authorization") String jwtToken,
            @RequestHeader("Kakao-Authorization") String kakaoAccessToken,
            @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(jwtToken, kakaoAccessToken, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
