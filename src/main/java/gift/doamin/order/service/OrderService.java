package gift.doamin.order.service;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import gift.doamin.order.repository.OrderRepository;
import gift.doamin.order.util.KakaoMessageRestClient;
import gift.doamin.product.entity.Option;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.dto.UserDto;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.KakaoOAuthTokenRepository;
import gift.doamin.user.repository.UserRepository;
import gift.doamin.user.service.OAuthService;
import gift.doamin.wishlist.repository.WishListRepository;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final KakaoOAuthTokenRepository oAuthTokenRepository;
    private final OAuthService oAuthService;
    private final KakaoMessageRestClient kakaoMessageRestClient;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
        WishListRepository wishListRepository, UserRepository userRepository,
        KakaoOAuthTokenRepository oAuthTokenRepository, OAuthService oAuthService,
        KakaoMessageRestClient kakaoMessageRestClient) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishListRepository = wishListRepository;
        this.userRepository = userRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthService = oAuthService;
        this.kakaoMessageRestClient = kakaoMessageRestClient;
    }

    @Transactional
    public OrderResponse makeOrder(UserDto userDto, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(() ->
            new NoSuchElementException("해당 옵션이 존재하지 않습니다."));
        User user = userRepository.findById(userDto.getId())
            .orElseThrow(UserNotFoundException::new);
        Order order = new Order(user, user, option, orderRequest.getQuantity(),
            orderRequest.getMessage());
        order = orderRepository.save(order);

        option.subtract(orderRequest.getQuantity());

        user.subtractPoint(option.getProduct().getPrice());

        subtractWishList(user, option, orderRequest.getQuantity());

        try {
            sendKakaoTalkMessage(order);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return new OrderResponse(order);
    }

    @Transactional
    public void subtractWishList(User user, Option option, Integer quantity) {
        wishListRepository.findByUserIdAndOptionId(user.getId(), option.getId())
            .ifPresent(wish -> {
                try {
                    wish.subtract(quantity);
                } catch (IllegalArgumentException e) {
                    wishListRepository.delete(wish);
                }
            });
    }

    @Transactional
    public void sendKakaoTalkMessage(Order order) {
        KakaoOAuthToken kakaoOAuthToken = oAuthTokenRepository.findByUser(order.getReceiver())
            .orElseThrow(() ->
                new IllegalArgumentException("카카오톡 사용자에게만 선물할 수 있습니다."));

        oAuthService.renewOAuthTokens(kakaoOAuthToken);

        kakaoMessageRestClient.sendMessage(kakaoOAuthToken.getAccessToken(), order.getMessage());
    }

    public Page<OrderResponse> getPage(Long userId, Pageable pageable) {
        return orderRepository.findAllBySenderId(userId, pageable).map(OrderResponse::new);
    }
}
