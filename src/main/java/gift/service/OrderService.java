package gift.service;

import gift.authentication.token.JwtResolver;
import gift.authentication.token.Token;
import gift.config.KakaoProperties;
import gift.domain.Order;
import gift.domain.vo.Point;
import gift.repository.OrderRepository;
import gift.web.client.KakaoClient;
import gift.web.client.dto.KakaoCommerce;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.response.order.CreateOrderResponse;
import gift.web.dto.response.order.OrderResponse;
import gift.web.dto.response.product.ReadProductResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final KakaoClient kakaoClient;
    private final KakaoProperties kakaoProperties;

    private final JwtResolver jwtResolver;

    private final ProductOptionService productOptionService;
    private final ProductService productService;
    private final MemberService memberService;
    private final OrderRepository orderRepository;

    private final double REWARD_POINT_RATE = 0.03; //주문 시, 3% 적립

    public OrderService(KakaoClient kakaoClient, JwtResolver jwtResolver, ProductOptionService productOptionService,
        ProductService productService, KakaoProperties kakaoProperties, MemberService memberService,
        OrderRepository orderRepository) {
        this.kakaoClient = kakaoClient;
        this.jwtResolver = jwtResolver;
        this.productOptionService = productOptionService;
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public CreateOrderResponse createOrder(String accessToken, Long memberId, CreateOrderRequest request) {
        //상품 옵션 수량 차감
        productOptionService.subtractOptionStock(request);

        Long optionId = request.getOptionId();
        Long productId = productOptionService.readProductOptionById(optionId).getProductId();

        //포인트 차감
        memberService.subtractPoint(memberId, request.getPoint());

        //주문 정보 저장
        Order order = orderRepository.save(request.toEntity(memberId, productId));

        //포인트 적립
        Point remainingPoint = memberService.addPoint(memberId, rewardPoint(order));

        sendOrderMessageIfSocialMember(accessToken, productId, request);
        return CreateOrderResponse.from(order, remainingPoint);
    }

    private int rewardPoint(Order order) {
        Integer price = productService.readProductById(order.getProductId()).getPrice();
        Integer quantity = order.getQuantity();

        int totalPrice = price * quantity;

        return (int) (totalPrice * REWARD_POINT_RATE);
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

    public List<OrderResponse> readOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
            .stream()
            .map(OrderResponse::from)
            .toList();
    }
}
