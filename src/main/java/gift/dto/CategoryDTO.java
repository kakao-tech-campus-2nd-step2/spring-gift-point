package gift.dto;

import gift.model.Category;

public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CategoryDTO getCategoryDTO(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
