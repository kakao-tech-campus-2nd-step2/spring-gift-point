package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.KakaoTokenService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KakaoTokenService kakaoTokenService;

    public OrderController(OrderService orderService, KakaoTokenService kakaoTokenService) {
        this.orderService = orderService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpSession session) throws JSONException {
        // 주문 처리 로직
        OrderResponse orderResponse = orderService.createOrder(orderRequest, session);

        //kakaoTokenService.processOrder(orderResponse);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
