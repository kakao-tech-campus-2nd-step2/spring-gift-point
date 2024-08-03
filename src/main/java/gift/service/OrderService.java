package gift.service;

import gift.common.dto.PageResponse;
import gift.common.enums.SocialType;
import gift.common.exception.ErrorCode;
import gift.common.exception.OAuthException;
import gift.common.exception.OptionException;
import gift.common.exception.ProductException;
import gift.common.exception.UserException;
import gift.common.util.KakaoUtil;
import gift.controller.order.dto.OrderRequest;
import gift.controller.order.dto.OrderResponse;
import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import gift.model.Token;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.TokenRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private KakaoUtil kakaoUtil;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, TokenRepository tokenRepository,
        UserRepository userRepository, ProductRepository productRepository,
        KakaoUtil kakaoUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.kakaoUtil = kakaoUtil;
    }

    @Transactional
    public OrderResponse.Info order(Long userId, OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.productId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));

        Order order = orderRepository.save(orderRequest.toEntity(option, product, userId));
        option.subtractQuantity(orderRequest.quantity());

        if (wishRepository.existsByProductIdAndUserId(option.getProduct().getId(), userId)) {
            wishRepository.deleteByProductIdAndUserId(option.getProduct().getId(), userId);
        }

        sendMessage(userId, orderRequest);

        return OrderResponse.Info.from(order);
    }

    @Transactional
    public PageResponse<OrderResponse.Info> getAllOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        List<OrderResponse.Info> responses = orders.getContent().stream().map(OrderResponse.Info::from).toList();
        return PageResponse.from(responses, orders);
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
