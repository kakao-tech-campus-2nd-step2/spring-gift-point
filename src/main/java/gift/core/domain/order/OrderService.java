package gift.core.domain.order;

import gift.core.PagedDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Order orderProduct(Order order, String gatewayAccessToken);

    PagedDto<Order> getOrdersOfUser(Long userId, Pageable pageable);

}
