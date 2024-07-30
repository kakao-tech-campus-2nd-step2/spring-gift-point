package gift.repository.product;

import gift.model.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersJpaRepository extends JpaRepository<Orders, Long>, OrdersRepository {

}
