package gift.controller;

import gift.domain.Member;
import gift.dto.OrderRequest;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order API", description = "주문 API 관련 엔드포인트")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "상품 주문 요청을 처리합니다.")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        try {
            orderService.placeOrder(orderRequest, member.getId());
            return ResponseEntity.ok("주문이 완료되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 실패: " + e.getMessage());
        }
    }
}
