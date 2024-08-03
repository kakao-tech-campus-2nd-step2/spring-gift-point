package gift.product.dto.wish;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WishResponse(
    @JsonProperty("id") Long productId,
    String name,
    int price,
    String imageUrl
) {

}
