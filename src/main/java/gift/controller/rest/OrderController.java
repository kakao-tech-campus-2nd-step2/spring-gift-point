package gift.controller.rest;

import gift.dto.order.OrderDTO;
import gift.dto.order.OrderResponseDto;
import gift.dto.response.MessageResponseDTO;
import gift.entity.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Order 컨트롤러", description = "Order API입니다.")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "모든 주문 조회", description = "모든 주문을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<OrderResponseDto> result = orderService.findAll(email)
                .stream()
                .map(order -> new OrderResponseDto(order))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "주문 생성", description = "주문을 생성합니다.")
    @PostMapping()
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) throws InterruptedException {
        Order order = orderService.save(session, orderDTO);
        return ResponseEntity.ok().body(new OrderResponseDto(order));
    }

    // 처리된 주문 제거
    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOrder(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        orderService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Order deleted successfully"));
    }
}
