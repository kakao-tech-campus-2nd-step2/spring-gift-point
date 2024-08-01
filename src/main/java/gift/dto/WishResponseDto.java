package gift.dto;

import gift.domain.Wish;
import java.time.LocalDateTime;

public record WishResponseDto(
    Long wishId,
    String name,
    int price,
    String imageUrl
    ) {

    public static WishResponseDto convertToDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getProduct().getName(),
            wish.getProduct().getPrice(),
            wish.getProduct().getImageUrl()
        );
    }
}
