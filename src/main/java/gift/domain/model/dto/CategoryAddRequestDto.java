package gift.domain.model.dto;

import gift.domain.model.entity.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryAddRequestDto {
    @NotBlank
    private String name;

    public CategoryAddRequestDto() {
    }

    public CategoryAddRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Category toEntity() {
        return new Category(this.name);
    }
}
