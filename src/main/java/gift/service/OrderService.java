package gift.service;

import gift.authentication.token.JwtResolver;
import gift.authentication.token.Token;
import gift.config.KakaoProperties;
import gift.domain.Order;
import gift.repository.OrderRepository;
import gift.web.client.KakaoClient;
import gift.web.client.dto.KakaoCommerce;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.response.order.OrderResponse;
import gift.web.dto.response.product.ReadProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final KakaoClient kakaoClient;
    private final KakaoProperties kakaoProperties;

    private final JwtResolver jwtResolver;

    private final ProductOptionService productOptionService;
    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(KakaoClient kakaoClient, JwtResolver jwtResolver, ProductOptionService productOptionService,
        ProductService productService, KakaoProperties kakaoProperties,
        OrderRepository orderRepository) {
        this.kakaoClient = kakaoClient;
        this.jwtResolver = jwtResolver;
        this.productOptionService = productOptionService;
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
        this.orderRepository = orderRepository;
    }

    /**
     * 주문을 생성합니다<br>
     * 카카오 로그인을 통해 서비스를 이용 중인 회원은 나에게 보내기를 통해 알림을 전송합니다.
     * @param accessToken 우리 서비스의 토큰
     * @param productId 구매할 상품 ID
     * @param memberId 구매자 ID
     * @param request 주문 요청
     * @return
     */
    @Transactional
    public OrderResponse createOrder(String accessToken, Long productId, Long memberId, CreateOrderRequest request) {
        //상품 옵션 수량 차감
        productOptionService.subtractOptionStock(request);

        //주문 정보 저장
        Order order = orderRepository.save(request.toEntity(memberId, productId));

        sendOrderMessageIfSocialMember(accessToken, productId, request);
        return OrderResponse.from(order);
    }

    /**
     * 소셜 로그인을 통해 주문한 경우 카카오톡 메시지를 전송합니다
     * @param accessToken 우리 서비스의 토큰
     * @param productId 상품 ID
     * @param request 주문 요청
     */
    private void sendOrderMessageIfSocialMember(String accessToken, Long productId, CreateOrderRequest request) {
        jwtResolver.resolveSocialToken(Token.fromBearer(accessToken))
            .ifPresent(socialToken ->
                kakaoClient.sendMessage(
                    kakaoProperties.getMessageUrlAsUri(),
                    getBearerToken(socialToken),
                    generateKakaoCommerce(productId, request).toJson()
                ));
    }

    private KakaoCommerce generateKakaoCommerce(Long productId, CreateOrderRequest request) {
        ReadProductResponse productResponse = productService.readProductById(productId);
        return KakaoCommerce.of(productResponse, request.getMessage());
    }

    private String getBearerToken(String token) {
        return "Bearer " + token;
    }
}
