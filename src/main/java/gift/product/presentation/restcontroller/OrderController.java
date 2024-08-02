package gift.product.presentation.restcontroller;

import gift.docs.product.OrderApiDocs;
import gift.global.authentication.annotation.MemberId;
import gift.product.business.service.OrderService;
import gift.product.presentation.dto.OrderRequest;
import gift.product.presentation.dto.OrderResponse;
import gift.product.presentation.dto.ProductResponse.Paging;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderApiDocs {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Long> createOrder(
        @MemberId Long memberId,
        @RequestBody @Valid OrderRequest.Create orderRequestCreate) {
        var orderInCreate = orderRequestCreate.toOrderInCreate(memberId);
        var orderId = orderService.createOrder(orderInCreate);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping
    public ResponseEntity<OrderResponse.Paging> getOrdersByPage(
        @PageableDefault(size = 20, sort = "createdDate", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "size", required = false) Integer size
    ) {
        if (size != null) {
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("size는 1~100 사이의 값이어야 합니다.");
            }
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }
        var orderOutPaging = orderService.getOrdersByPage(pageable);
        var orderResponsePaging = OrderResponse.Paging.from(orderOutPaging);
        return ResponseEntity.ok(orderResponsePaging);
    }

}
