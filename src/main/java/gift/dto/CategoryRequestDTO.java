package gift.dto;

import gift.model.Category;

public class CategoryRequestDTO {
    private String name;

    public CategoryRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Category toEntity() {
        return new Category(this.name);
    }
}
