package gift.service;

import gift.client.KakaoApi;
import gift.dto.OrderPageResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final KakaoApi kakaoApi;

    public OrderService(OrderRepository orderRepository, ProductOptionRepository productOptionRepository,
                        UserRepository userRepository, WishRepository wishRepository,
                        KakaoApi kakaoApi) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
        this.kakaoApi = kakaoApi;
    }

    @Transactional
    public OrderResponseDto createOrder(User loginUser, String kakaoAccessToken, OrderRequestDto requestDto) {
        ProductOption productOption = findProductOptionById(requestDto.getProductOptionId());

        productOption.decreaseQuantity(requestDto.getQuantity());
        productOptionRepository.save(productOption);

        Order order = saveOrder(loginUser, productOption, requestDto);

        removeWish(loginUser, productOption);

        sendOrderConfirmationMessage(kakaoAccessToken, order);

        return new OrderResponseDto(order.getId(), productOption.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    @Transactional(readOnly = true)
    public OrderPageResponseDto getUserOrders(Long userId, Pageable pageable) {
        User user = findUserById(userId);
        Page<Order> orders = orderRepository.findByUser(user, pageable);

        return new OrderPageResponseDto(
                orders.getContent().stream()
                        .map(order -> new OrderResponseDto(order.getId(), order.getProductOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage()))
                        .collect(Collectors.toList()),
                orders.getNumber(),
                orders.getTotalPages(),
                orders.getTotalElements()
        );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
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
