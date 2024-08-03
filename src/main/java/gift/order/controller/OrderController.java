package gift.order.controller;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.dto.OrderSortField;
import gift.order.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<Page<OrderResponseDto>> getOrders(
      @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
      @RequestParam(defaultValue = "ORDER_DATE_TIME") @Parameter(description = "정렬 필드", example = "ORDER_DATE_TIME") OrderSortField sort,
      @RequestParam(defaultValue = "DESC") @Parameter(description = "정렬 방향", example = "DESC") Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getFieldName()));
    Page<OrderResponseDto> orders = orderService.getOrders(pageable);
    return ResponseEntity.ok(orders);
  }

  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(
      @Valid @RequestBody OrderRequestDto orderRequestDto) {
    OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
    return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
  }
}

