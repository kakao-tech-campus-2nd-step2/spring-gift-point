package gift.service;

import gift.domain.Option;
import gift.domain.Order;
import gift.dto.OrderDTO;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoOAuthService kakaoOAuthService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishRepository wishRepository, KakaoOAuthService kakaoOAuthService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @Transactional
    public OrderDTO createOrder(Long memberId, Long optionId, int quantity, String message) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        if (option.quantityLessThan(quantity)) {
            throw new IllegalArgumentException("Insufficient stock for the option");
        }

        option.subtractQuantity(quantity);
        optionRepository.save(option);

        wishRepository.deleteByMemberIdAndProductId(memberId, option.getProduct().getId());

        Order order = new Order.OrderBuilder()
            .option(option)
            .quantity(quantity)
            .orderDateTime(LocalDateTime.now())
            .message(message)
            .build();

        Order savedOrder = orderRepository.save(order);

        kakaoOAuthService.sendOrderMessageToMe(savedOrder);

        return toDTO(savedOrder);
    }

    private OrderDTO toDTO(Order order) {
        return new OrderDTO.OrderDTOBuilder()
            .id(order.getId())
            .optionId(order.getOption().getId())
            .quantity(order.getQuantity())
            .orderDateTime(order.getOrderDateTime())
            .message(order.getMessage())
            .build();
    }
}
