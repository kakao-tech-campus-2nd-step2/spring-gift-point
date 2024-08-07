package gift.controller;

import gift.dto.KakaoTokenDto;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Member;
import gift.entity.OrderItem;
import gift.entity.Product;
import gift.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "[협업] ORDER API", description = "[협업] 주문 컨트롤러")
public class OrderNewController {

    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;
    private final OrderService orderService;
    private final MemberService memberService;
    private final PointService pointService;

    @Autowired
    public OrderNewController(
            OptionService optionService,
            WishlistService wishlistService,
            KakaoTokenService kakaoTokenService,
            KakaoService kakaoService,
            OrderService orderService,
            MemberService memberService,
            PointService pointService

    ) {
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
        this.orderService = orderService;
        this.memberService = memberService;
        this.pointService = pointService;
    }


    @PostMapping
    @Operation(summary = "상품 주문", description = "상품을 주문하고 메시지를 보냅니다.")
    public ResponseEntity<OrderResponse> orderItem(@RequestBody OrderRequest request) {
        Long userId = Long.valueOf("1");

        optionService.subtractOptionQuantity(request.getOptionId(), request.getQuantity());

        Member member = memberService.getMemberById(userId);

        pointService.subtractPoint(member, request.getPoint());

        Product product = optionService.getProductById(request.getOptionId());
        int price = product.getPrice();
        pointService.addPoint(member, (int) ((price - request.getPoint()) * 0.1));

        wishlistService.deleteWishlistItem(member.getEmail(), product.getId());
        KakaoTokenDto tokenDto = kakaoTokenService.getTokenByEmail(member.getEmail());
        String accessToken = tokenDto.getAccessToken();
        kakaoService.sendKakaoMessage(accessToken, request.getMessage());
        OrderItem order = orderService.getOrder(request.getOptionId());
        return ResponseEntity.ok().body(new OrderResponse(request.getOptionId()));
    }
}
