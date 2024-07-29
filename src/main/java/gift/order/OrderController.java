package gift.order;

import gift.jwt.OAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "order", description = "order API")
@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문하기", description = "주문한다")
    @PostMapping()
    public ResponseEntity<OrderInfo> order(@RequestBody OrderRequest orderRequest, @OAuth String accessToken){
        OrderInfo order = orderService.saveOrder(orderRequest);
        orderService.sendMessage(orderRequest, accessToken);
        orderService.deleteOrderedProduct(orderRequest,  1L);

        return ResponseEntity.ok(order);
    }



}
