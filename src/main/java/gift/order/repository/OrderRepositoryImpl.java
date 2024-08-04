package gift.order.repository;

import gift.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }


    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }


    public Order findById(Long id) {
        return orderJpaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found")
        );
    }

    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        return orderJpaRepository.findByUserId(userId, pageable);
    }
}
