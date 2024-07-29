package gift.product.dto;

import gift.product.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    @NotBlank(message = "카테고리 이름은 필수 입력사항입니다.")
    private String name;

    public CategoryDTO() {}

    public CategoryDTO(String name) {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category convertToDomain() {
        return new Category(this.name);
    }

    public Category convertToDomain(Long id) {
        return new Category(id, this.name);
    }
}
