package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Order.OrderLogResponse;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.User.UserResponse;
import gift.security.AuthenticateMember;
import gift.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /*
     * 주문하기 ( 주문 추가 )
     */
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> order(
            @RequestBody OrderRequest orderRequest,
            @AuthenticateMember UserResponse user
    ) throws JsonProcessingException {
        OrderResponse order = orderService.order(orderRequest, user);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    /*
     * 주문 내역 조회
     */
    @GetMapping("/api/orders")
    public ResponseEntity<Page<OrderLogResponse>> orderLog(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort
    ){
        Page<OrderLogResponse> orders = orderService.findAll(page, size, sort.getFirst(), sort.getLast());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
