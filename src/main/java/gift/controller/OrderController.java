package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OrderController", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
    String accessToken = token.replace("Bearer ", "");
    return orderService.placeOrder(orderRequest, accessToken);
  }
}