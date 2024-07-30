package gift.order.repository;

import gift.order.domain.Order;

public interface OrderRepository {
    public Order save(Order order);

    public Order findById(Long id);
}
