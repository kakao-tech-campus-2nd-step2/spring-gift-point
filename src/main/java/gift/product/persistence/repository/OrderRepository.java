package gift.product.persistence.repository;

import gift.product.persistence.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Long saveOrder(Order order);

    Page<Order> getOrdersByPage(Pageable pageable);
}
