package gift.controller;

import gift.dto.KakaoTokenDto;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Order;
import gift.entity.Product;
import gift.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "[협업] ORDER API", description = "[협업] 주문 컨트롤러")
public class OrderNewController {

    private OptionService optionService;
    private WishlistService wishlistService;
    private KakaoTokenService kakaoTokenService;
    private KakaoService kakaoService;
    private OrderService orderService;
    @Autowired
    public OrderNewController(
            OptionService optionService,
            WishlistService wishlistService,
            KakaoTokenService kakaoTokenService,
            KakaoService kakaoService,
            OrderService orderService

    ) {
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
        this.orderService = orderService;
    }


    @PostMapping
    @Operation(summary = "상품 주문", description = "상품을 주문")
    public ResponseEntity<Void> orderItem(@RequestBody OrderRequest request,
                                          @RequestAttribute("userId") Long userId) {
        Long optionId = request.getOptionId();
        int quantity = request.getQuantity();
        optionService.subtractOptionQuantity(optionId, quantity);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping
    @Operation(summary = "상품 주문", description = "상품을 주문하고 메시지를 보냅니다.")
    public ResponseEntity<OrderResponse> orderItem(@RequestParam("email") String email, @RequestBody OrderRequest request) {
        optionService.subtractOptionQuantity(request.getOptionId(), request.getQuantity());
        Product product = optionService.getProductById(request.getOptionId());
        int price = product.getPrice();
        wishlistService.deleteWishlistItem(email, product.getId());
        KakaoTokenDto tokenDto = kakaoTokenService.getTokenByEmail(email);
        String accessToken = tokenDto.getAccessToken();
        kakaoService.sendKakaoMessage(accessToken, request.getMessage());
        Order order = orderService.getOrder(request.getOptionId());
        return ResponseEntity.ok().body(new OrderResponse(order.getId(), request.getOptionId(), request.getQuantity(), request.getMessage()));
    }
}
