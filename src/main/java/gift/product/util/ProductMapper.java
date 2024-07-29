package gift.product.util;

import gift.product.entity.Category;
import gift.product.entity.Product;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;

import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponse toResponseDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
                       .getName(),
                product.getOptions()
                       .stream()
                       .map(OptionMapper::toResponseDto)
                       .collect(Collectors.toUnmodifiableSet())
        );
    }

    public static Product toEntity(ProductRequest request, Category category) {
        return new Product(
                request.name(),
                request.price(),
                request.imageUrl(),
                category
        );
    }

}
