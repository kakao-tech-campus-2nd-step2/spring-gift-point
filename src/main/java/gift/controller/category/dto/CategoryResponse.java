package gift.controller.category.dto;

import gift.model.Category;

public record CategoryResponse(Long id, String name, String color, String imageUrl, String description) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }
}
