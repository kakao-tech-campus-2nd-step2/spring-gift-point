package gift.domain.controller;

import gift.domain.annotation.ValidMember;
import gift.domain.controller.apiResponse.OrderCreateApiResponse;
import gift.domain.controller.apiResponse.OrdersGetApiResponse;
import gift.domain.dto.request.OrderRequest;
import gift.domain.entity.Member;
import gift.domain.service.OrderService;
import gift.global.apiResponse.SuccessApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<OrdersGetApiResponse> getOrders(@ValidMember Member member) {
        return SuccessApiResponse.ok(new OrdersGetApiResponse(HttpStatus.OK, orderService.getOrders(member)));
    }

    @PostMapping
    public ResponseEntity<OrderCreateApiResponse> createOrder(@ValidMember Member member, @Valid @RequestBody OrderRequest request) {
        var created = HttpStatus.CREATED;
        return SuccessApiResponse.of(new OrderCreateApiResponse(created, orderService.createOrder(member, request)), created);
    }
}
