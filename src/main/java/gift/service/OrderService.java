package gift.service;

import gift.client.KakaoApi;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;
import gift.entity.ProductOption;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import gift.value.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final KakaoApi kakaoApi;
    private final TokenService tokenService;

    public OrderService(OrderRepository orderRepository, ProductOptionRepository productOptionRepository,
                        UserRepository userRepository, WishRepository wishRepository,
                        KakaoApi kakaoApi, TokenService tokenService) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
        this.kakaoApi = kakaoApi;
        this.tokenService = tokenService;
    }

    @Transactional
    public OrderResponseDto createOrder(String jwtToken, String kakaoAccessToken, OrderRequestDto requestDto) {
        Token jwt = new Token(jwtToken);
        Token kakaoToken = new Token(kakaoAccessToken);

        Map<String, String> userInfo = tokenService.extractUserInfo(jwt.getToken());
        String userId = userInfo.get("id");

        User user = findUserById(userId);
        ProductOption productOption = findProductOptionById(requestDto.getProductOptionId());

        productOption.decreaseQuantity(requestDto.getQuantity());
        productOptionRepository.save(productOption);

        Order order = saveOrder(user, productOption, requestDto);

        removeWish(user, productOption);

        sendOrderConfirmationMessage(kakaoToken.getToken(), order);

        return new OrderResponseDto(order.getId(), productOption.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    private User findUserById(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private ProductOption findProductOptionById(Long productOptionId) {
        return productOptionRepository.findById(productOptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
    }

    private Order saveOrder(User user, ProductOption productOption, OrderRequestDto requestDto) {
        Order order = new Order(productOption, user, requestDto.getQuantity(), LocalDateTime.now(), requestDto.getMessage());
        orderRepository.save(order);
        return order;
    }

    private void removeWish(User user, ProductOption productOption) {
        wishRepository.deleteByUserAndProduct(user, productOption.getProduct());
    }

    private void sendOrderConfirmationMessage(String kakaoAccessToken, Order order) {
        kakaoApi.sendMessageToMe(kakaoAccessToken, order);
    }
}
