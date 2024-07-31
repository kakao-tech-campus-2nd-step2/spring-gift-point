package gift.Controller;

import gift.DTO.OrderDto;
import gift.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderDto> orderOption(@RequestBody OrderDto orderDto)
    throws IllegalAccessException {
    OrderDto orderdDto = orderService.orderOption(orderDto);
    var location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(orderdDto.getId())
      .toUri();
    return ResponseEntity.created(location).body(orderdDto);
  }

}
