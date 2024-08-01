package gift.product.category.dto.response;

import gift.product.category.entity.Category;

public record CategoryResponse(
    Long id,
    String name,
    String description,
    String color,
    String imageUrl
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription(),
            category.getColor(), category.getImageUrl());
    }

}
