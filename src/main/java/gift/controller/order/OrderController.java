package gift.controller.order;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.model.user.User;
import gift.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderSpecification {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse.Info> order(@RequestAttribute("user") User user,
                                               @Valid @RequestBody OrderRequest.Create orderRequest) {
        OrderResponse.Info orderResponse = orderService.order(user.getId(), orderRequest.productId(), orderRequest);
        orderService.sendMessage(orderRequest, user, orderRequest.productId());
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<PagingResponse<OrderResponse.DetailInfo>> getOrderList(@RequestAttribute("user") User user,
                                                                                 @ModelAttribute PagingRequest pagingRequest){
        PagingResponse<OrderResponse.DetailInfo> response = orderService.getOrderList(user.getId(),pagingRequest.getPage(),pagingRequest.getSize());
        return ResponseEntity.ok(response);
    }
}