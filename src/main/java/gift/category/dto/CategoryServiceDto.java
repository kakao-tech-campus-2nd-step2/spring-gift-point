package gift.category.dto;

import gift.category.domain.*;

public record CategoryServiceDto(Long id, CategoryName name, CategoryColor color, CategoryImageUrl imageUrl, CategoryDescription description) {
    public Category toCategory() {
        return new Category(id, name, color, imageUrl, description);
    }
}
