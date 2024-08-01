package gift.dto.wishproduct;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.product.ProductBasicInformation;

public record WishProductResponse(
        Long id,
        @JsonProperty("product")
        ProductBasicInformation productBasicInformation
) {
    public static WishProductResponse of(Long id, ProductBasicInformation productBasicInformation) {
        return new WishProductResponse(id, productBasicInformation);
    }
}
