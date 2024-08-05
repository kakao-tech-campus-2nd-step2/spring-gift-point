package gift.controller;

import gift.authentication.LoginMember;
import gift.authentication.UserDetails;
import gift.dto.OrderRequestDto;
import gift.dto.SuccessResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 등록 API")
    @ApiResponse(responseCode = "201", description = "주문 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping
    public ResponseEntity<SuccessResponse> makeOrder(@LoginMember UserDetails userDetails, @RequestBody OrderRequestDto request) {
        orderService.handleOrder(userDetails.id(), request);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.CREATED, "주문에 성공하였습니다"));
    }
}
