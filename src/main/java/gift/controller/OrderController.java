package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.DomainResponse;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.model.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "APIs related to order operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<DomainResponse> createOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        Order order = orderService.createOrder(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage(), member);
        OrderResponse response = new OrderResponse(order);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", response.getId());
        responseMap.put("optionId", response.getOptionId());
        responseMap.put("quantity", response.getQuantity());
        responseMap.put("message", response.getMessage());
        responseMap.put("orderDateTime", response.getOrderDateTime());
        HttpResult httpResult = new HttpResult(HttpStatus.CREATED.value(), "Order created successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(responseMap), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @Operation(summary = "주문 목록 조회(페이지네이션 적용)", description = "주문 목록을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<DomainResponse> getOrders(Pageable pageable) {
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        List<Map<String, Object>> orderList = orders.stream()
                .map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", order.getId());
                    map.put("optionId", order.getOptionId());
                    map.put("quantity", order.getQuantity());
                    map.put("message", order.getMessage());
                    map.put("orderDateTime", order.getOrderDateTime());
                    return map;
                })
                .collect(Collectors.toList());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Orders retrieved successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, orderList, HttpStatus.OK));
    }
}