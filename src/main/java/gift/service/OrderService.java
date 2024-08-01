package gift.service;

import gift.model.Order;

public interface OrderService {
    Order createOrder(Order order, String kakaoToken);
}