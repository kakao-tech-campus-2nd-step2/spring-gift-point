package gift.DTO;

import gift.Model.Entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Schema(description = "찜 응답 DTO")
public record ResponseWishDTO(
        Long id,
        String name,
        int price,
        String imageUrl,
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
