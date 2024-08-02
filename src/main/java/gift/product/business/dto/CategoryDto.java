package gift.product.business.dto;

import gift.product.persistence.entity.Category;
import java.util.List;

public record CategoryDto(
    Long id,
    String name,
    String description,
    String color,
    String imageUrl
) {

    public static CategoryDto from(Category category) {
        return new CategoryDto(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getColor(),
            category.getImageUrl()
        );
    }

    public static List<CategoryDto> of(List<Category> categories) {
        return categories.stream()
            .map(CategoryDto::from)
            .toList();
    }
}
