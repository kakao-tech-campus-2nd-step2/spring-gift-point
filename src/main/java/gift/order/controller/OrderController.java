package gift.order.controller;

import gift.annotation.LoginUser;
import gift.order.dto.request.OrderRequest;
import gift.order.dto.response.OrderResponse;
import gift.order.service.OrderService;
import gift.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문", description = "주문을 위한 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문", description = "주문을 요청하고, 성공하면 카카오톡 메시지를 보냅니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 요청 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 주문 실패"),
        @ApiResponse(responseCode = "500", description = "요청 처리 중 서버 오류로 인한 문제 발생")
    })
    public ResponseEntity<OrderResponse> createOrder(@LoginUser User user,
        @RequestBody OrderRequest orderRequest) {
        var response = orderService.createOrder(user, orderRequest);
        URI location = UriComponentsBuilder.fromPath("/api/orders/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
