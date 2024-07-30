package gift.dto;

import gift.model.Category;

public class CategoryResponseDTO {
    private Long id;
    private String name;

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}
