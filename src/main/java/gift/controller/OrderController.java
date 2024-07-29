
package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.Member;
import gift.model.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "APIs related to order operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "주문 생성", description = "새로운 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        Order order = orderService.createOrder(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage(), member);
        OrderResponse response = new OrderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}