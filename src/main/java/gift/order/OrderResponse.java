package gift.order;

public record OrderResponse (Long id, Long optionId, Long quantity, String orderDateTime, String message){
}
