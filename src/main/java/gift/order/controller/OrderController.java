package gift.order.controller;

import gift.option.service.OptionService;
import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.service.OrderService;
import gift.wish.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;
    private final OptionService optionService;

    private final WishlistService wishlistService;

    public OrderController(OrderService orderService, OptionService optionService,
        WishlistService wishlistService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
    }

    @PostMapping
    @Operation(summary = "order 가져오기")
    public ResponseEntity<OrderResponse> getOrder(
        @RequestBody OrderRequest orderRequest,
        @RequestParam("userId") Long userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String jwtAccessToken = authorizationHeader.replace("Bearer ", "");
        // 1. 주문 저장
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        // 2. 옵션 수량 차감, wishlist에서 제거
        optionService.subtract(optionService.convertToDTO(optionService.findById(orderResponse.optionid())),
                                orderResponse.quantity());
        wishlistService.deleteByUserIdProductId(userId, optionService.findById(orderResponse.optionid()).getProduct().getId());
        // 3. 카카오톡 메시지 api 전송
        orderService.sendMessage(jwtAccessToken, orderRequest.message());
        // 4. response 반환
        return ResponseEntity.ok(orderResponse);
    }
}
