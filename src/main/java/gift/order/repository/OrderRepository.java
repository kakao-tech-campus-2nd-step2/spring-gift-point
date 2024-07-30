package gift.order.repository;

import gift.global.MyCrudRepository;
import gift.order.domain.Order;
import gift.order.domain.OrderMessage;

import java.util.List;

public interface OrderRepository extends MyCrudRepository<Order, Long> {
    boolean existsById(Long id);
}
