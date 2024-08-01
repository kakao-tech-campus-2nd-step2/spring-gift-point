package gift.repository;

import gift.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaDao extends JpaRepository<Order, Long> {

}
