package gift.controller;

import gift.classes.RequestState.OrderRequestStateDTO;
import gift.classes.RequestState.RequestStatus;
import gift.dto.OrderDto;
import gift.dto.RequestOrderDto;
import gift.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController", description = "Order API")
public class OrderController {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 추가", description = "KaKao 로그인 후, 주문을 추가하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 추가 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<OrderRequestStateDTO> addOrder(
        @RequestBody RequestOrderDto requestOrderDto,
        @RequestHeader("Authorization") String token) {
        OrderDto orderDto = orderService.addOrder(requestOrderDto, token);
        return ResponseEntity.ok().body(new OrderRequestStateDTO(
            RequestStatus.success,
            null,
            orderDto
        ));
    }
}
