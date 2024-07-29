package gift.dto;

import gift.vo.Order;

import java.time.LocalDateTime;

public record KakaoMessageRequestDto (
        Long id,
        Long optionId,
        int quantity,
        LocalDateTime orderDateTime,
        String message,
        String productName,
        String optionName
) {
    public static KakaoMessageRequestDto toKakaoMessageRequestDto(Order order, String productName, String optionName) {
        return new KakaoMessageRequestDto(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                productName,
                optionName);
    }
}
