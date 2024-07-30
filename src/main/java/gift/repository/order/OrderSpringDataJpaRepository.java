package gift.repository.order;

import gift.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSpringDataJpaRepository extends JpaRepository<Order, Long> {
}
