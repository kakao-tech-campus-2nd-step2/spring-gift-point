package gift.domain;

import gift.entity.WishEntity;
import jakarta.validation.constraints.Min;

public class Wish {

    public record WishRequest(
        @Min(1)
        Long productId
    ) {

    }

    public record WishResponse(
        Long id,
        Long memberId,
        Long productId,
        String name,
        Long price,
        String imageUrl
    ) {
        public static WishResponse from(WishEntity wishEntity) {
            return new WishResponse(
                wishEntity.getId(),
                wishEntity.getMemberEntity().getId(),
                wishEntity.getProductEntity().getId(),
                wishEntity.getProductEntity().getName(),
                wishEntity.getProductEntity().getPrice(),
                wishEntity.getProductEntity().getImageUrl()
            );
        }
    }


}
