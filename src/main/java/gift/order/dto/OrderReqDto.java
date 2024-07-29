package gift.order.dto;

public record OrderReqDto(
        Long optionId,
        Integer quantity,
        String message
) {

}
