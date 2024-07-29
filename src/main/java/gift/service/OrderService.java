package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        optionService.decreaseOptionQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        Order order = new Order.Builder()
                .optionId(orderRequest.getOptionId())
                .quantity(orderRequest.getQuantity())
                .orderTime(orderRequest.getOrderTime())
                .message(orderRequest.getMessage())
                .email(orderRequest.getEmail())
                .build();
        order = orderRepository.save(order);

        wishService.deleteByProductId(orderRequest.getProductId());

        return new OrderResponse.Builder()
                .id(order.getId())
                .optionId(order.getOptionId())
                .quantity(order.getQuantity())
                .orderTime(order.getOrderTime())
                .message(order.getMessage())
                .build();
    }
}
