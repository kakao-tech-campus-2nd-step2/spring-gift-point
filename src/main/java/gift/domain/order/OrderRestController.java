package gift.domain.order;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import gift.domain.member.dto.LoginInfo;
import gift.domain.order.dto.request.OrderRequest;
import gift.domain.order.dto.response.OrderPageResponse;
import gift.global.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "카카오 주문하기, 주문 후 메시지 전송")
    public void order(
        @Valid @RequestBody OrderRequest orderRequest,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        orderService.order(orderRequest, loginInfo);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회")
    public ResponseEntity<OrderPageResponse> getOrders(
        @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "createdDate_desc") String sort,
        @Login LoginInfo loginInfo) {
        Sort sortObj = getSortObject(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);
        OrderPageResponse orderPageResponse = orderService.getOrders(loginInfo.getId(), pageRequest);

        return ResponseEntity.ok(orderPageResponse);
    }
    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(ASC, "price");
            case "price_desc":
                return Sort.by(DESC, "price");
            case "createdDate_asc":
                return Sort.by(ASC, "createdDate");
            default:
                return Sort.by(DESC, "createdDate");
        }
    }
}
