package gift.service;

import gift.dto.OrderDto;
import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishlistRepository wishlistRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository, OptionService optionService,
                        WishlistRepository wishlistRepository, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistRepository = wishlistRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public Order createOrder(OrderDto orderDto, Long memberId) {
        Option option = optionService.findOptionById(orderDto.getOptionId());

        if (option.getQuantity() < orderDto.getQuantity()) {
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
        }
        optionService.decreaseOptionQuantity(orderDto.getOptionId(), orderDto.getQuantity());

        Order order = new Order(option, orderDto.getQuantity(), orderDto.getMessage());
        orderRepository.save(order);
        Product product = optionService.findProductByOptionId(option.getId());

        wishlistRepository.deleteByMemberIdAndProductId(memberId, product.getId());

        kakaoMessageService.sendMessageToKakao(order, memberId);
        return order;
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
