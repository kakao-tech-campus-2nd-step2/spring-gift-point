package gift.product.util;

import gift.product.dto.CategoryRequest;
import gift.product.dto.CategoryResponse;
import gift.product.entity.Category;

public class CategoryMapper {

    public static CategoryResponse toResponseDto(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }

    public static Category toEntity(CategoryRequest request) {
        return new Category(
                request.name(),
                request.color(),
                request.imageUrl(),
                request.description()
        );
    }

}
