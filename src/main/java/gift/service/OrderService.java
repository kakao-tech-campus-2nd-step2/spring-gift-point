package gift.service;

import gift.model.Member;
import gift.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order createOrder(Order order, Member member);
    Page<Order> getOrders(Pageable pageable);
}
