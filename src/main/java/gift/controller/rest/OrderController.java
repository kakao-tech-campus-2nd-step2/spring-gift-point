package gift.controller.rest;

import gift.entity.order.Order;
import gift.entity.order.OrderDTO;
import gift.entity.product.Product;
import gift.entity.response.MessageResponseDTO;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order 컨트롤러", description = "Order API입니다.")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "모든 주문 조회", description = "모든 주문을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 주문 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Order.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<Order> orders = orderService.findAll(email);
        return ResponseEntity.ok().body(orders);
    }

    @Operation(summary = "주문 생성", description = "주문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 옵션, 유저 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Order order = orderService.save(session, orderDTO);
        return ResponseEntity.ok().body(order);
    }

    // 처리된 주문 제거
    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 제거 성공",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 주문 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOrder(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        orderService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Order deleted successfully"));
    }
}
