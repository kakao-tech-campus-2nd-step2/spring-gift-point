package gift.category.dto;

import gift.category.domain.CategoryColor;
import gift.category.domain.CategoryDescription;
import gift.category.domain.CategoryImageUrl;
import gift.category.domain.CategoryName;

import java.util.Objects;

public record CategoryRequestDto(CategoryName name, CategoryColor color, CategoryImageUrl imageUrl, CategoryDescription description) {
    public CategoryServiceDto toCategoryServiceDto() {
        return new CategoryServiceDto(null, this.name, this.color, this.imageUrl, this.description);
    }

    public CategoryServiceDto toCategoryServiceDto(Long id) {
        return new CategoryServiceDto(id, this.name, this.color, this.imageUrl, this.description);
    }
}
