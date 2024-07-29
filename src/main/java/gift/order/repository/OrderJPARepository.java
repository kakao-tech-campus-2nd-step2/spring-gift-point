package gift.order.repository;

import gift.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJPARepository extends JpaRepository<Order, Long> {
    Order findByOptionId(Long requestOptionId);
}
