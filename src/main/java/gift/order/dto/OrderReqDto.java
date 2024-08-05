package gift.order.dto;

public record OrderReqDto(
        Long optionId,
        Integer quantity,
        Integer points,
        String message
) {

}
