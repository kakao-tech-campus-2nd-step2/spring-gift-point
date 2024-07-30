package gift.service.impl;

import gift.model.Order;
import gift.repository.OrderRepository;
import gift.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order, String kakaoToken) {
        order.setOrderDateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }
}