package gift.controller;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.enums.SortDirection;
import gift.model.Order;
import gift.service.OrderService;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            String email = memberService.extractEmailFromToken(token.substring(7)); // JWT 토큰에서 이메일 추출
            Order order = orderService.placeOrder(email, orderRequestDTO);
            OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getOrderList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "orderDateTime") String sortBy,
        @RequestParam(defaultValue = "DESC") SortDirection direction,
        @RequestHeader("Authorization") String token) {

        Sort sort = Sort.by(direction == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<OrderResponseDTO> orderList = orderService.getOrderList(token, pageRequest);
        return ResponseEntity.ok(orderList);
    }


}
