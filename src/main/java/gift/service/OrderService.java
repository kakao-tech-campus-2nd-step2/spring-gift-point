package gift.service;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }

    public Page<OrderResponse> getOrdersByEmail(String email, Pageable pageable) {
        Page<Order> orders = orderRepository.findByEmail(email, pageable);

        return orders.map(order -> new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        ));
    }
}
