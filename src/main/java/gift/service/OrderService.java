package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OrderMessageService orderMessageService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, OrderMessageService orderMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.orderMessageService = orderMessageService;
    }

    @Transactional
    public OrderResponse addOrder(OrderRequest orderRequest, String token) {
        Option option = validateAndUpdateOption(orderRequest);
        Order order = createOrder(orderRequest, option);
        wishRepository.deleteByOptionId(orderRequest.getOptionId());
        orderMessageService.sendOrderMessage(order, token);

        return getOrderResponse(order);
    }

    public Slice<OrderResponse> getPagedOrders(Pageable pageable) {
        Slice<Order> ordersSlice = orderRepository.findBy(pageable);
        return ordersSlice.map(order -> new OrderResponse(order.getId(), order.getOptionId(),
            order.getQuantity(), order.getOrderDateTime(), order.getMessage()));
    }

    private Option validateAndUpdateOption(OrderRequest orderRequest) {
        Option option = optionRepository.findWithId(orderRequest.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        if (option.getQuantity() < orderRequest.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        option.subtractQuantity(orderRequest.getQuantity());
        optionRepository.save(option);
        return option;
    }

    private Order createOrder(OrderRequest orderRequest, Option option) {
        Order order = new Order(orderRequest.getOptionId(), orderRequest.getQuantity(),
            LocalDateTime.now(), orderRequest.getMessage());
        order = orderRepository.save(order);
        return order;
    }

    private OrderResponse getOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDateTime(), order.getMessage());
    }

}
