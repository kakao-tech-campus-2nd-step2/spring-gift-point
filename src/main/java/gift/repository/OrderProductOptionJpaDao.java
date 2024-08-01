package gift.repository;

import gift.entity.OrderProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductOptionJpaDao extends JpaRepository<OrderProductOption, Long> {

}
