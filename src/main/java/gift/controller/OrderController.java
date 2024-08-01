package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.model.dto.OrderDTO;
import gift.model.form.OrderForm;
import gift.service.OrderService;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Api(카카오 로그인 필요)")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Operation(summary = "상품 주문", description = "특정 상품을 주문합니다.")
    @PostMapping
    public ResponseEntity<?> handleOrderToMe(@RequestBody OrderForm form,
        @RequestAttribute("userId") Long userId)
        throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO(form, userId);
        orderService.executeOrder(userId, orderDTO);
        return ResponseEntity.ok().build();
    }
}
