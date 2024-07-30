package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Order;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/{optionId}")
@Tag(name = "Order", description = "주문 API")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, KakaoService kakaoService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @Operation(summary = "주문 추가", description = "해당 회원이 해당 상품에 대한 주문 추가")
    public ResponseEntity<?> addOrder(
        HttpServletRequest request,
        @Valid @RequestBody Order order,
        @RequestParam("optionId") Long optionId) throws JsonProcessingException {
        Long memberId = jwtUtil.extractMemberId(request);
        Order addedOrder = orderService.addOrder(memberId, order, optionId);
        kakaoService.sendOrderMessage(memberId, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedOrder);
    }

}
