package gift.core.domain.order;

import gift.core.PagedDto;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Order save(Order order);

    PagedDto<Order> findAll(Pageable pageable);

}
