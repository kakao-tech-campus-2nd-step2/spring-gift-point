package gift.controller.order;

import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.controller.option.OptionResponse;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.controller.response.PageInfo;
import gift.controller.wish.WishPageResponse;
import gift.service.AuthService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order", description = "Order API")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "get All orders", description = "모든 주문 조회")
    public ResponseEntity<ApiResponseBody<OrderPageResponse>> getAllOrders(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        var targets = orderService.findAll(pageable);
        PageInfo pageInfo = new PageInfo(targets.getPageable().getPageNumber(),
            targets.getTotalElements(), targets.getTotalPages());
        return new ApiResponseBuilder<OrderPageResponse>()
            .httpStatus(HttpStatus.OK)
            .data(new OrderPageResponse(pageInfo, targets.toList()))
            .messages("")
            .build();
    }

    @PostMapping
    @Operation(summary = "create Order", description = "주문 생성")
    public ResponseEntity<ApiResponseBody<OrderResponse>> createOrder(@LoginUser LoginResponse member, @RequestBody OrderRequest order) {
        return new ApiResponseBuilder<OrderResponse>()
            .httpStatus(HttpStatus.OK)
            .messages("주문 생성")
            .data(orderService.save(member.id(), member.id(), order))
            .build();
    }
}