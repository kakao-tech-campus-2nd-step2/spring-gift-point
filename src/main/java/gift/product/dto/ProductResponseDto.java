package gift.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.product.domain.Product;

public record ProductResponseDto(Long id, String name, Long price, @JsonProperty("image_url") String imageUrl,
                                 Long categoryId) {
    public static ProductResponseDto productToProductResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName().getProductNameValue(), product.getPrice().getProductPriceValue(), product.getImageUrl().getImageUrlValue(), product.getCategory().getId());
    }
}
