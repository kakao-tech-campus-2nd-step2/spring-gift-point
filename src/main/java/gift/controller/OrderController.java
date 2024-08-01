package gift.controller;

import gift.domain.Member;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 API 관련 엔드포인트")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "상품 주문 요청을 처리합니다.")
    public ResponseEntity<Object> placeOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        try {
            OrderResponse orderResponse = orderService.placeOrder(orderRequest, member);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 실패: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "회원의 주문 목록을 조회합니다.")
    public ResponseEntity<Page<OrderResponse>> getOrderList(@LoginMember Member member,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "orderDateTime,desc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<OrderResponse> orders = orderService.getOrdersByMemberId(member.getId(), pageRequest);
        return ResponseEntity.ok(orders);
    }
}
