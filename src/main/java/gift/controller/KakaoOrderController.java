package gift.controller;

import gift.dto.KakaoTokenDto;
import gift.service.KakaoService;
import gift.service.KakaoTokenService;
import gift.service.OptionService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/products/")
@Tag(name = "Product API", description = "상품 관련 API")
public class KakaoOrderController {

    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;

    @Autowired
    public KakaoOrderController(OptionService optionService, WishlistService wishlistService, KakaoService kakaoService, KakaoTokenService kakaoTokenService) {
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @PostMapping("/order/{productId}")
    @Operation(summary = "상품 주문", description = "상품을 주문하고 메시지를 보냅니다.")
    public ResponseEntity<Void> orderItem(@RequestParam("email") String email, @RequestParam("optionId") Long optionId, @RequestParam("quantity") int quantity, @PathVariable Long productId, @RequestParam("message") String message) {
        optionService.subtractOptionQuantity(optionId, quantity);
        wishlistService.deleteWishlistItem(email, productId);
        System.out.println(message);
        KakaoTokenDto tokenDto = kakaoTokenService.getTokenByEmail(email);
        String accessToken = tokenDto.getAccessToken();
        kakaoService.sendKakaoMessage(accessToken, message);
        return ResponseEntity.ok().build();
    }
}