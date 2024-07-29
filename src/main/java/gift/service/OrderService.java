package gift.service;

import gift.config.KakaoProperties;
import gift.dto.OrderDetailResponse;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final KakaoMessageService kakaoMessageService;
    private final KakaoProperties kakaoProperties;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        UserRepository userRepository, KakaoMessageService kakaoMessageService,
        KakaoProperties kakaoProperties) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.kakaoProperties = kakaoProperties;
    }

    @Transactional
    public OrderResponse createOrder(User user, OrderRequest request) {
        Option option = optionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new OptionNotFoundException("선택한 옵션이 존재하지 않습니다."));
        Product product = optionRepository.findProductByOptionId(request.getOptionId())
            .orElseThrow(() -> new ProductNotFoundException("해당 옵션을 가진 상품이 존재하지 않습니다."));

        Order order = orderRepository.save(
            new Order(option, user, request.getQuantity(), LocalDateTime.now(),
                request.getMessage()));

        option.subtractQuantity(request.getQuantity());
        user.subtractWishNumber(request.getQuantity(), product);

        if (kakaoProperties.isKakaoLoginCompleted()) { //kakaologin이 수행되지 않으면 accessToken이 지정되지 않아 메시지를 보내지 않음
            kakaoMessageService.sendOrderMessage(request.getMessage(), product.getName(),
                request.getQuantity(), request.getTotalPrice(product));
        }

        return new OrderResponse(order.getId(), option.getId(), request.getQuantity(),
            LocalDateTime.now(),
            request.getMessage());
    }

    public List<OrderDetailResponse> getAllOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
            .map(order -> new OrderDetailResponse(order.getId(), order.getOption(), order.getUser(),
                order.getQuantity(), order.getLocalDateTime(), order.getMessage()))
            .collect(Collectors.toList());

    }
}
