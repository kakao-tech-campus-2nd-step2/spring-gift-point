package gift.model;

public record OrderResponse(long optionId, long totalPrice, long discountedPrice, long accumulatedPoint) {

}
