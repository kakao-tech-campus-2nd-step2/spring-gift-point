package gift.order.service;

import gift.core.domain.order.Order;

public interface OrderPointStrategy {

    Long calculatePoint(Order order);

}
