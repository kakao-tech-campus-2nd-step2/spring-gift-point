package gift.controller.restcontoller;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.CommonResponse;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "새로운 주문을 생성합니다")
    @PostMapping
    public ResponseEntity<CommonResponse> order(@LoginMember MemberRequest memberRequest, @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.orderOption(orderRequest, memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(orderResponse, "주문 생성 성공", true));
    }

    @Operation(summary = "주문 목록을 페이지 단위로 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getPagedOrders(@LoginMember MemberRequest memberRequest,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "orderDateTime,desc") String sort){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(orderService.getPagedOrders(memberRequest, page, size, sort), "페이징 된 주문 목록 조회 성공", true));
    }
}
