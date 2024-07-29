package gift.category.dto;

import gift.category.entity.Category;

public record CategoryResDto(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description
) {

    public CategoryResDto(Category category) {
        this(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }
}
