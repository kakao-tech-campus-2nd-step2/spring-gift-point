package gift.controller;

import gift.dto.OrderDTO;
import gift.dto.ProductDTO;
import gift.service.JwtUtil;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 주문 API")
@RestController
@RequestMapping("/api/product/order")
public class OrderController {
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @Autowired
    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{optionid}")
    @Operation(summary = "상품 주문", description = "상품 주문 시 카카오톡 메시지가 발송됩니다.")
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token, @RequestBody OrderDTO order) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(parsedToken);
        String email = jwtUtil.extractEmail(parsedToken);
        String access_token = jwtUtil.extractKakaoToken(parsedToken);

        orderService.createOrder(order, userId, email, access_token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "해당 사용자의 주문 목록을 페이지네이션으로 조회합니다.")
    public ResponseEntity<Page<OrderDTO>> getOrder(
        @RequestHeader("Authorization") String token,
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        @RequestParam(value = "page", defaultValue = "0") int page,
        @Min(value = 1, message = "한 페이지당 1개 이상의 항목이 포함되어야 합니다.")
        @RequestParam(value = "size", defaultValue = "10") int size) {

        String parsedToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(parsedToken);

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orders = orderService.getUserOrders(userId, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
