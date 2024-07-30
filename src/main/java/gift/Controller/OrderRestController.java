package gift.Controller;

import gift.Model.request.OrderRequest;
import gift.Model.response.OrderResponse;
import gift.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "주문 API", description = "주문과 관련된 API")
@RestController
@RequestMapping("/api")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService){
        this.orderService = orderService;
    }

    @Operation(summary = "주문", description = "accessToken과 orderRequest를 받아 주문하며, 카카오톡의 나에게 메시지 보내기 기능을 실행한다.")
    @Parameter(name="accessToken", description = "카카오의 인증 토큰으로, 유효한 토큰을 보내야 나에게 메시지가 보내진다.")
    @Parameter(name="orderRequest", description = "주문으로, 수량이 남아있을 경우 차감된다.")
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> order(@RequestAttribute("Email") String email, @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.order(orderRequest);
        // orderService.sendKakaoTalkMessage(email, orderResponse);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/orders")
    public Page<OrderResponse> readPage(@RequestAttribute("Email") String email, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "sort", defaultValue = "10") String sort){
        return orderService.getPage(email, page, size, sort);
    }
}
