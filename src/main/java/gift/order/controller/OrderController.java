package gift.order.controller;

import gift.auth.LoginMember;
import gift.member.dto.MemberResDto;
import gift.order.dto.OrderReqDto;
import gift.order.dto.OrderResDto;
import gift.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 API", description = "주문 관리 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "모든 주문 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공"),
            })
    public ResponseEntity<Page<OrderResDto>> getOrders(@LoginMember MemberResDto memberDto, Pageable pageable) {
        Page<OrderResDto> orders = orderService.getOrders(memberDto, pageable);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            })
    public ResponseEntity<OrderResDto> createOrder(
            @LoginMember MemberResDto memberDto,
            @RequestBody OrderReqDto orderReqDto
    ) {
        OrderResDto order = orderService.createOrder(memberDto, orderReqDto);
        return ResponseEntity.created(URI.create("/api/orders/" + order.id())).body(order);
    }

    @DeleteMapping("/{order-id}")
    @Operation(summary = "주문 취소", description = "주문을 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            })
    public ResponseEntity<String> cancelOrder(
            @LoginMember MemberResDto memberDto,
            @PathVariable("order-id") Long orderId
    ) {
        orderService.cancelOrder(memberDto, orderId);
        return ResponseEntity.ok("주문을 취소했습니다.");
    }
}
