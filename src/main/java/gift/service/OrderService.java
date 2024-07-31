package gift.service;

import gift.exception.option.NotFoundOptionsException;
import gift.exception.order.NotFoundOrderException;
import gift.model.Options;
import gift.model.Order;
import gift.repository.OptionsRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.response.OrderResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final OptionsService optionsService;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository, OptionsService optionsService,
        WishRepository wishRepository, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionsService = optionsService;
        this.wishRepository = wishRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    public List<OrderResponse> getOrder(Long memberId) {
        return orderRepository.findByMemberId(memberId)
            .stream()
            .map(OrderResponse::createOrderResponse)
            .toList();
    }

    public OrderResponse makeOrder(Long memberId, String accessToken, Long productId, Long optionId, Integer quantity, String message) {
        OrderResponse response = addOrder(memberId, productId, optionId, quantity, message);
        sendKakaoMessageToMe(accessToken, message);
        return response;
    }

    @Transactional
    public OrderResponse addOrder(Long memberId, Long productId, Long optionId, Integer quantity,
        String message) {
        Options option = optionsService.getOption(optionId);
        optionsService.subtractQuantity(option.getId(), quantity, productId);
        wishRepository.findByMemberIdAndProductId(memberId, productId)
            .ifPresent(wishRepository::delete);
        Order savedOrder = orderRepository.save(new Order(memberId, option, quantity, message));

        return OrderResponse.createOrderResponse(savedOrder);
    }

    public void sendKakaoMessageToMe(String accessToken, String message) {
        kakaoMessageService.sendMessageToMe( accessToken, message);
    }

}
