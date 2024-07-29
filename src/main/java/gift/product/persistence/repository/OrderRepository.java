package gift.product.persistence.repository;

import gift.product.persistence.entity.Order;

public interface OrderRepository {

    Long saveOrder(Order order);
}
