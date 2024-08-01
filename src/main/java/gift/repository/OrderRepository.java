package gift.repository;

import gift.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);
    List<Order> findAll();
}
