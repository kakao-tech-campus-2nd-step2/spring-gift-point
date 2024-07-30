package gift.api.category.dto;

import gift.api.category.domain.Category;

public record CategoryRequest(
   String name,
   String color,
   String imageUrl,
   String description
) {

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
