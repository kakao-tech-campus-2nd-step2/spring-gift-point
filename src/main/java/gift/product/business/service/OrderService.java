package gift.product.business.service;

import gift.global.domain.OAuthProvider;
import gift.global.exception.custrom.NotFoundException;
import gift.member.business.service.WishlistService;
import gift.member.persistence.repository.MemberRepository;
import gift.oauth.business.client.KakaoApiClient;
import gift.oauth.business.dto.KakaoOrderMessage;
import gift.product.business.dto.OptionIn;
import gift.product.business.dto.OrderIn;
import gift.product.persistence.repository.OrderRepository;
import gift.product.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final KakaoApiClient kakaoApiClient;

    public OrderService(ProductService productService, WishlistService wishlistService,
        OrderRepository orderRepository, ProductRepository productRepository,
        MemberRepository memberRepository, KakaoApiClient kakaoApiClient) {
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public Long createOrder(OrderIn.Create orderInCreate) {
        var optionInSubtract = new OptionIn.Subtract(
            orderInCreate.optionId(),
            orderInCreate.quantity()
        );
        productService.subtractOption(optionInSubtract, orderInCreate.productId());

        try {
            wishlistService.deleteWishList(orderInCreate.memberId(), orderInCreate.productId());
        } catch (NotFoundException ignored) {
        }

        var product = productRepository.getProductById(orderInCreate.productId());
        var member = memberRepository.getMemberById(orderInCreate.memberId());
        var order = orderInCreate.toOrder(product, member);
        var orderId = orderRepository.saveOrder(order);

        if(member.getOAuthProvider() == OAuthProvider.KAKAO) {
            var accessToken = member.getAccessToken();
            var kakaoOrderMessage = KakaoOrderMessage.TemplateObject.of(
                product.getName() + " 주문 완료",
                "localhost",
                product.getPrice()
            );
            kakaoApiClient.sendOrderMessage(accessToken, kakaoOrderMessage);
        }

        return orderId;
    }
}
