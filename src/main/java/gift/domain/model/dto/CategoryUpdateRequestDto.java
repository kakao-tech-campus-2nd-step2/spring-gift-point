package gift.domain.model.dto;

import gift.domain.model.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryUpdateRequestDto {

    @NotBlank
    private String name;

    public CategoryUpdateRequestDto() {
    }

    public CategoryUpdateRequestDto(String name) {
        this.name = name;
    }

    public Category toEntity() {
        return new Category(name);
    }

    public String getName() {
        return name;
    }
}
