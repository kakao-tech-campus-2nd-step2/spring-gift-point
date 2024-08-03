package gift.doamin.order.repository;

import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllBySenderId(Long senderId, Pageable pageable);
}
