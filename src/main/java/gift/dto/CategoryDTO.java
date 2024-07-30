package gift.dto;

import gift.entity.Category;

public class CategoryDTO {

    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public Category toEntity() {
        return new Category(name);
    }
}
