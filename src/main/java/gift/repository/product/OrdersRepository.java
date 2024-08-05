package gift.repository.product;

import gift.model.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersRepository {

    Orders save(Orders orders);

    Page<Orders> findAllByMemberId(Long memberId, Pageable pageable);
}
