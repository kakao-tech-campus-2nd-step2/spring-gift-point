package gift.domain.product.dto;

import gift.domain.product.entity.Product;
import java.util.List;

public record ProductResponse(
    Long id,
    CategoryResponse category,
    String name,
    int price,
    String imageUrl,
    List<OptionResponse> options
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            CategoryResponse.from(product.getCategory()),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getOptions().stream().map(OptionResponse::from).toList()
        );
    }
}
