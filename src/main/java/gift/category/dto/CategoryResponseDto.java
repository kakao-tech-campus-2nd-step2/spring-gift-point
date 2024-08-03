package gift.category.dto;

import gift.category.entity.Category;

public record CategoryResponseDto(
    long id,
    String name,
    String imageUrl
) {

    public Category toCategory() {
        return new Category(id, name, imageUrl);
    }

    public static CategoryResponseDto fromCategory(Category category) {
        return new CategoryResponseDto(category.getCategoryId(), category.getName(),
            category.getImageUrl());
    }
}
