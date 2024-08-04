package gift.web.controller.api;

import gift.authentication.annotation.LoginMember;
import gift.service.OrderService;
import gift.web.dto.MemberDetails;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.response.order.OrderResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> orderProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
        @RequestBody @Validated CreateOrderRequest request,
        @LoginMember MemberDetails memberDetails
    ) {
        OrderResponse response = orderService.createOrder(accessToken, memberDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> readOrders(@PageableDefault Pageable pageable) {
        List<OrderResponse> response = orderService.readOrders(pageable);
        return ResponseEntity.ok(response);
    }
}
