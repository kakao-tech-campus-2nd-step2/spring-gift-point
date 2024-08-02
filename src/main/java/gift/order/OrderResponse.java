package gift.order;

public record OrderResponse(Long id,
                            Integer totalPrice,
                            Integer discountedPrice,
                            Integer accumulatedPoint) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getTotalPrice(),
            order.getDiscountedPrice(),
            order.getAccumulatedPoint()
        );
    }
}
