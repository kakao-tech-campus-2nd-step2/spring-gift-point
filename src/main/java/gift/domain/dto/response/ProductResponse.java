package gift.domain.dto.response;

import gift.domain.entity.Product;
import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    Integer price,
    String imageUrl,
    CategoryResponse category,
    List<OptionResponse> options
) {

    public static ProductResponse of(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            CategoryResponse.of(product.getCategory()),
            OptionResponse.of(product.getOptions()));
    }
}
