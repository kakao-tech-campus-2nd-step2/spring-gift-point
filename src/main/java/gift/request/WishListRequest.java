package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record WishListRequest(
    @NotNull(message = "상품 id를 입력해주세요.")
    @JsonProperty("product_id")
    Long productId,
    @NotNull(message = "위시리스트 수량을 입력해주세요.")
    Integer quantity
) {

}
