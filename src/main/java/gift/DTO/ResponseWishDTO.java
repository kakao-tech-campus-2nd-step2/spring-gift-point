package gift.DTO;

import gift.Model.Entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Schema(description = "찜 응답 DTO")
public record ResponseWishDTO(
        @Schema(description = "상품 id")
        Long id,

        @Schema(description = "상품 이름")
        String name,

        @Schema(description = "상품 가격")
        int price,

        @Schema(description = "상품 이미지Url")
        String imageUrl,

        @Schema(description = "찜한 시간")
        LocalDateTime createdDate
) {
    public static ResponseWishDTO of(Wish wish){
        return new ResponseWishDTO(wish.getProduct().getId(),
                wish.getProduct().getName().getValue(),
                wish.getProduct().getPrice().getValue(),
                wish.getProduct().getImageUrl().getValue(),
                wish.getCreatedDate());
    }
}
