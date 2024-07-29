package gift.dto.response;

import gift.domain.Category;

public record CategoryResponse(Long id, String name, String color, String imageUrl, String description) {
    public static CategoryResponse from(final Category category){
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

    public Category toEntity() {
        return new Category(this.id, this.name, this.color, this.imageUrl, this.description);
    }
}
