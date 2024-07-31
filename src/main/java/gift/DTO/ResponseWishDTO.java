package gift.DTO;

import gift.Model.Entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "찜 응답 DTO")
public class ResponseWishDTO {
    @Schema(description = "상품 이름")
    private String name;

    public ResponseWishDTO() {
    }

    public ResponseWishDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ResponseWishDTO of(Wish wish){
        return new ResponseWishDTO(wish.getProduct().getName().getValue());
    }
}
