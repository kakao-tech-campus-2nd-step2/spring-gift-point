package gift.order;

import gift.common.auth.LoginMember;
import gift.common.auth.LoginMemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Order 를 관리하는 API")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 생성", description = "주문을 생성하고, 주문 메세지를 보냅니다.")
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest,
        @LoginMember LoginMemberDto loginMemberDto) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, loginMemberDto);
        return ResponseEntity.created(URI.create("/api/orders/" + orderResponse.id()))
            .body(orderResponse);
    }
}
