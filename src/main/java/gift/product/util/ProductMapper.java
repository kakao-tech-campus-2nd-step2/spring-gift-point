package gift.product.util;

import gift.product.dto.GetProductResponse;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.entity.Category;
import gift.product.entity.Product;

public class ProductMapper {

    public static ProductResponse toResponseDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
                       .getId()
        );
    }

    public static GetProductResponse toGetResponseDto(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                CategoryMapper.toResponseDto(product.getCategory())
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
