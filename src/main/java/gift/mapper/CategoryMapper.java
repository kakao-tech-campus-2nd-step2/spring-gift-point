package gift.mapper;

import gift.dto.CategoryResponseDto;
import gift.entity.Category;

public class CategoryMapper {

    public static CategoryResponseDto toCategoryResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }
}