package gift.service;

import gift.common.enums.SocialType;
import gift.common.exception.ErrorCode;
import gift.common.exception.OAuthException;
import gift.common.exception.OptionException;
import gift.common.exception.UserException;
import gift.common.util.KakaoUtil;
import gift.controller.order.dto.OrderRequest;
import gift.controller.order.dto.OrderResponse;
import gift.model.Option;
import gift.model.Order;
import gift.model.Token;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.TokenRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private KakaoUtil kakaoUtil;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, TokenRepository tokenRepository,
        UserRepository userRepository, KakaoUtil kakaoUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.kakaoUtil = kakaoUtil;
    }

    @Transactional
    public OrderResponse order(Long userId, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));

        Order order = orderRepository.save(orderRequest.toEntity(option));
        option.subtractQuantity(orderRequest.quantity());

        if (wishRepository.existsByProductIdAndUserId(option.getProduct().getId(), userId)) {
            wishRepository.deleteByProductIdAndUserId(option.getProduct().getId(), userId);
        }

        sendMessage(userId, orderRequest);

        return OrderResponse.from(order);
    }

    private void sendMessage(Long userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (user.checkSocialType(SocialType.KAKAO)) {
            Token token = tokenRepository.findByUserId(userId)
                .orElseThrow(() -> new OAuthException(ErrorCode.TOKEN_NOT_FOUND));

            kakaoUtil.checkExpiredAccessToken(token);
            kakaoUtil.sendMessage(token.getAccessToken(), orderRequest.message());
        }
    }
}
