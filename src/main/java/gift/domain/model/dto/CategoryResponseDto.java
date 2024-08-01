package gift.domain.model.dto;

import gift.domain.model.entity.Category;

public class CategoryResponseDto {

    private final Long id;
    private final String name;

    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
