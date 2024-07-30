package gift.order.service;

import gift.core.PagedDto;
import gift.core.domain.order.Order;
import gift.core.domain.order.OrderRepository;
import gift.core.domain.order.OrderService;
import gift.core.domain.product.ProductOption;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.exception.OptionNotFoundException;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final OrderAlarmGateway orderAlarmGateway;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductOptionRepository productOptionRepository,
            UserRepository userRepository,
            OrderAlarmGateway orderAlarmGateway
    ) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.userRepository = userRepository;
        this.orderAlarmGateway = orderAlarmGateway;
    }

    @Override
    @Transactional
    public Order orderProduct(Order order, String gatewayAccessToken) {
        if (!userRepository.existsById(order.userId())) {
            throw new UserNotFoundException();
        }
        Long orderedProductId = productOptionRepository.getProductIdByOptionId(order.optionId());
        ProductOption option = productOptionRepository
                .findById(order.optionId())
                .orElseThrow(OptionNotFoundException::new);

        option.validateOrderable(order.quantity());
        productOptionRepository.save(orderedProductId, option.applyDecreaseQuantity(order.quantity()));
        Order orderResult = orderRepository.save(order);
        orderAlarmGateway.sendAlarm(gatewayAccessToken, orderResult.message());
        return orderResult;
    }

    @Override
    public PagedDto<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
