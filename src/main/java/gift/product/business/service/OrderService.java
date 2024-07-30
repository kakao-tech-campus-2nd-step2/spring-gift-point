package gift.product.business.service;

import gift.global.domain.OAuthProvider;
import gift.global.exception.custrom.NotFoundException;
import gift.member.business.service.MemberService;
import gift.member.business.service.WishlistService;
import gift.product.business.dto.KakaoOrderMessage;
import gift.product.business.client.KakaoMessageClient;
import gift.product.business.dto.OptionIn;
import gift.product.business.dto.OrderIn;
import gift.product.business.dto.OrderOut;
import gift.product.persistence.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final KakaoMessageClient kakaoMessageClient;

    public OrderService(ProductService productService, WishlistService wishlistService,
        OrderRepository orderRepository, MemberService memberService,
        KakaoMessageClient kakaoMessageClient) {
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.kakaoMessageClient = kakaoMessageClient;
    }

    @Transactional
    public Long createOrder(OrderIn.Create orderInCreate) {
        var optionInSubtract = new OptionIn.Subtract(
            orderInCreate.optionId(),
            orderInCreate.quantity()
        );
        productService.subtractOption(optionInSubtract, orderInCreate.productId());

        try {
            wishlistService.deleteWishListByMemberAndProduct(orderInCreate.memberId(), orderInCreate.productId());
        } catch (NotFoundException ignored) {
        }

        var product = productService.getProductById(orderInCreate.productId());
        var member = memberService.getMemberById(orderInCreate.memberId());
        var order = orderInCreate.toOrder(product, member);
        var orderId = orderRepository.saveOrder(order);

        if (member.getOAuthProvider() == OAuthProvider.KAKAO) {
            var accessToken = member.getAccessToken();
            var kakaoOrderMessage = KakaoOrderMessage.TemplateObject.of(
                product.getName() + " 주문 완료",
                "localhost",
                product.getPrice()
            );
            kakaoMessageClient.sendOrderMessage(accessToken, kakaoOrderMessage);
        }

        return orderId;
    }

    @Transactional(readOnly = true)
    public OrderOut.Paging getOrdersByPage(Pageable pageable) {
        var orderPage = orderRepository.getOrdersByPage(pageable);
        return OrderOut.Paging.from(orderPage);
    }
}
