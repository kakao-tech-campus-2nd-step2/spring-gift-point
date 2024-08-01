package gift.controller;

import gift.dto.OrderDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
  @PostMapping
  public ResponseEntity<OrderDto> createOrder(
          @RequestHeader("Authorization") String authorization,
          @RequestHeader("Kakao-Authorization") String kakaoAuthorization,
          @RequestBody OrderDto orderDto) {
    OrderDto createdOrder = orderService.createOrder(orderDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
  }

  @Operation(summary = "주문 목록 조회", description = "주문 목록을 페이지 단위로 조회한다.")
  @GetMapping
  public ResponseEntity<Page<OrderDto>> getOrders(
          @RequestHeader("Authorization") String authorization,
          @RequestHeader("Kakao-Access-Token") String kakaoAccessToken,
          @RequestParam(defaultValue = "0") int page) {
    Page<OrderDto> orders = orderService.getOrders(PageRequest.of(page, 10));
    return ResponseEntity.ok(orders);
  }
}