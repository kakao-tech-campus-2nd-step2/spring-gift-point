package gift.domain.dto.response;

import gift.domain.entity.Product;
import java.util.List;

public record ProductWithCategoryIdResponse(
    Long id,
    String name,
    Integer price,
    String imageUrl,
    Long categoryId,
    List<OptionResponse> options
) {

    public static ProductWithCategoryIdResponse of(Product product) {
        return new ProductWithCategoryIdResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            OptionResponse.of(product.getOptions()));
    }

    public static ProductWithCategoryIdResponse of(ProductResponse productResponse) {
        return new ProductWithCategoryIdResponse(
            productResponse.id(),
            productResponse.name(),
            productResponse.price(),
            productResponse.imageUrl(),
            productResponse.category().id(),
            productResponse.options());
    }
}
