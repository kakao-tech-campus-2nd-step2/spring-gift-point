package gift.product.persistence.repository;

import gift.product.persistence.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Long saveOrder(Order order) {
        return orderJpaRepository.save(order).getId();
    }
}
