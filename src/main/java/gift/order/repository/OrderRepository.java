package gift.order.repository;

import gift.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderRepository {
    public Order save(Order order);

    public Order findById(Long id);

    Page<Order> findByUserId(Long userId, Pageable pageable);
}
