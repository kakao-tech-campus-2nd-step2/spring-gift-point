package gift.controller;

import gift.domain.OrderDTO;
import gift.entity.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Tag(name = "주문", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestHeader("Authorization") String token, @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        var order = orderService.addOrder(token, orderDTO);
        URI uri = new URI("/api/orders" + order.getId());
        return ResponseEntity.created(uri).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        var order = orderService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

}
