package gift.doamin.order.controller;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.service.OrderService;
import gift.doamin.user.entity.User;
import gift.global.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문", description = "선물하기 주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문하기", description = "선물하기 주문을 요청합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponse makeOrder(@Valid @RequestBody OrderRequest orderRequest,
        @LoginUser User user) {

        OrderResponse orderResponse = orderService.makeOrder(user, orderRequest);
        System.out.println("orderResponse = " + orderResponse);
        return orderResponse;
    }

}
