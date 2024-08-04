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

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final OrderPointStrategy orderPointStrategy;
    private final PointOperationSupport pointOperationSupport;
    private final OrderAlarmGateway orderAlarmGateway;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductOptionRepository productOptionRepository,
            UserRepository userRepository,
            PointOperationSupport pointOperationSupport,
            OrderPointStrategy orderPointStrategy,
            OrderAlarmGateway orderAlarmGateway
    ) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.userRepository = userRepository;
        this.pointOperationSupport = pointOperationSupport;
        this.orderPointStrategy = orderPointStrategy;
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
        pointOperationSupport.subtractPoint(order.userId(), order.usedPoint());
        productOptionRepository.save(orderedProductId, option.applyDecreaseQuantity(order.quantity()));
        Order orderResult = orderRepository.save(order);
        Long givingPoint = orderPointStrategy.calculatePoint(order);
        pointOperationSupport.addPoint(order.userId(), givingPoint);
        orderAlarmGateway.sendAlarm(gatewayAccessToken, orderResult.message());
        return orderResult;
    }

    @Override
    public PagedDto<Order> getOrdersOfUser(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }
}
