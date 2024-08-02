package gift.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Product;

public record ProductResponse(
    Long id,
    String name,
    int price,
    @JsonProperty(value = "image_url") String imageUrl,
    @JsonProperty(value = "category_name") String categoryName
) {

    private ProductResponse(Product product) {
        this(product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategoryName()
        );
    }

    public static ProductResponse createProductResponse(Product product) {
        return new ProductResponse(product);
    }
}
