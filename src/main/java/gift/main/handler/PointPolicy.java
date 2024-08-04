package gift.main.handler;

import gift.main.entity.Order;

public class PointPolicy {

    public static int calculatePoints(Order order) {
        if (order.getUsingPoint() != 0) {
            return -order.getUsingPoint();
        }
        return (int) (order.getTotalPrice() * 0.1);
    }

    private PointPolicy() {

    }
}
