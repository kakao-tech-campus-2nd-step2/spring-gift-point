package gift.domain.dto.request;

import gift.domain.entity.Category;

public record CategoryRequest(String name, String color, String imageUrl, String description) {

    public static CategoryRequest of(Category category) {
        return new CategoryRequest(category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
