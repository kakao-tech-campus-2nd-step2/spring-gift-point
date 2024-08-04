package gift.model.product;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductResponse(Long id, String name, int price, @JsonProperty("image_url") String imageUrl, @JsonProperty("category_id") Long categoryId) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }

}
