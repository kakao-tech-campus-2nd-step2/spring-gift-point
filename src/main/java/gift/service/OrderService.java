package gift.service;

import gift.dto.OrderDetailResponse;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.exception.MinimumOptionException;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoMessageService kakaoMessageService;
    private final Double discountPercent = 0.05;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse createOrder(User user, OrderRequest request) {
        Option option = optionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new OptionNotFoundException("선택한 옵션이 존재하지 않습니다."));
        Product product = optionRepository.findProductByOptionId(request.getOptionId())
            .orElseThrow(() -> new ProductNotFoundException("해당 옵션을 가진 상품이 존재하지 않습니다."));

        int discountPrice = (int) (request.getTotalPrice(product) * (1 - discountPercent));
        subtractPoint(user, discountPrice);

        Order order = orderRepository.save(
            new Order(option, user, request.getQuantity(), LocalDateTime.now(),
                request.getMessage()));

        option.subtractQuantity(request.getQuantity());
        user.deleteWish(product);

        if (user.isKakaoLoginCompleted()) { //kakaologin이 수행되지 않으면 accessToken이 지정되지 않아 메시지를 보내지 않음
            kakaoMessageService.sendOrderMessage(request.getMessage(), product.getImageUrl(),
                product.getName(), option.getName(), request.getQuantity(),
                request.getTotalPrice(product), discountPrice, user.getAccessToken());
        }

        return new OrderResponse(order.getId(), option.getId(), request.getQuantity(),
            LocalDateTime.now(),
            request.getMessage());
    }

    public Page<OrderDetailResponse> getAllOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        return orders.map(order -> new OrderDetailResponse(
            order.getId(),
            order.getOption(),
            order.getUser(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        ));
    }

    private void subtractPoint(User user, Integer discountPrice) {
        if (discountPrice > user.getPoint()) {
            throw new MinimumOptionException("포인트 잔액이 부족합니다.");
        }

        user.subtractPoint(discountPrice);
    }
}
