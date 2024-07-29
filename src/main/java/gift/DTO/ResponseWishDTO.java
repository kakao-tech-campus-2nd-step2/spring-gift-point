package gift.DTO;

import gift.Model.Entity.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "찜 응답 DTO")
public class ResponseWishDTO {
    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "찜한 수량")
    private int count;

    public ResponseWishDTO() {
    }

    public ResponseWishDTO(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public static ResponseWishDTO of(Wish wish){
        return new ResponseWishDTO(wish.getProduct().getName().getValue(), wish.getCount().getValue());
    }
}
