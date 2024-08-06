package gift.order.service;

import gift.common.client.kakao.KakaoApiClient;
import gift.common.client.kakao.KakaoProperties;
import gift.order.dto.KakaoMessageRequestBody;
import gift.order.dto.PaymentInfo;
import gift.order.dto.TemplateArgs;
import gift.order.dto.request.OrderRequest;
import gift.order.dto.response.OrderResponse;
import gift.order.entity.Order;
import gift.order.repository.OrderJpaRepository;
import gift.product.option.entity.Option;
import gift.product.option.service.OptionService;
import gift.user.entity.User;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    private final OrderJpaRepository orderRepository;

    private final OptionService optionService;
    private final WishRepository wishRepository;

    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;

    public OrderService(OrderJpaRepository orderRepository, OptionService optionService,
        WishRepository wishRepository, KakaoProperties kakaoProperties,
        KakaoApiClient kakaoApiClient) {
        this.orderRepository = orderRepository;

        this.optionService = optionService;
        this.wishRepository = wishRepository;

        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public OrderResponse createOrder(User user, OrderRequest request) {
        var option = optionService.subtractOptionQuantity(request.optionId(), request.quantity());

        wishRepository.findByUser(user)
            .stream()
            .filter(wish -> wish.existsProduct(option))
            .forEach(wishRepository::delete);

        var templateArgs = TemplateArgs.of(request.message(), option);
        var kakaoMessageRequest = new KakaoMessageRequestBody(kakaoProperties.templateId(),
            templateArgs);

        Order savedOrder = orderRepository.save(new Order(pay(user, option, request)));

        kakaoApiClient.sendMessage(user.getAccessToken(), kakaoMessageRequest);

        return OrderResponse.from(savedOrder);
    }

    private PaymentInfo pay(User user, Option option, OrderRequest request) {
        var totalPrice = option.getPrice() * request.quantity();

        // 포인트 사용
        var discountedPrice = 0;
        if (request.usePoint()) {
            user.usePoint(request.point());
            discountedPrice = request.point();
        }
        var payedPrice = totalPrice - discountedPrice;
        int accumulatedPoint = payedPrice / 10;

        // 결제 로직(생략)

        user.chargePoint(accumulatedPoint);

        return PaymentInfo.of(option, request, totalPrice, discountedPrice, payedPrice,
            accumulatedPoint);
    }

}
