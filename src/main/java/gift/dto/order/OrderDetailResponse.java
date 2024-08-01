package gift.dto.order;

public class OrderDetailResponse {
    private final OrderResponse order;
    private final int pointsUsed;
    private final int pointsReceived;

    public OrderDetailResponse(OrderResponse order, int pointsUsed, int pointsReceived) {
        this.order = order;
        this.pointsUsed = pointsUsed;
        this.pointsReceived = pointsReceived;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public int getPointUsed() {
        return pointsUsed;
    }

    public int getPointsReceived() {
        return pointsReceived;
    }
}
