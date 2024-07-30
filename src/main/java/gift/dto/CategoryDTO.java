package gift.dto;

import gift.domain.Category;
import gift.domain.CategoryName;

public class CategoryDTO {

    private Long id;
    private CategoryName name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryDTO() {
    }

    private CategoryDTO(CategoryDTOBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
    }

    public static CategoryDTO from(Category category) {
        return new CategoryDTOBuilder()
            .id(category.getId())
            .name(category.getName())
            .color(category.getColor())
            .imageUrl(category.getImageUrl())
            .description(category.getDescription())
            .build();
    }

    public Category toEntity() {
        return new Category.CategoryBuilder()
            .id(this.id)
            .name(this.name)
            .color(this.color)
            .imageUrl(this.imageUrl)
            .description(this.description)
            .build();
    }

    public Long getId() {
        return id;
    }

    public CategoryName getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public static class CategoryDTOBuilder {
        private Long id;
        private CategoryName name;
        private String color;
        private String imageUrl;
        private String description;

        public CategoryDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryDTOBuilder name(CategoryName name) {
            this.name = name;
            return this;
        }

        public CategoryDTOBuilder color(String color) {
            this.color = color;
            return this;
        }

        public CategoryDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CategoryDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryDTO build() {
            return new CategoryDTO(this);
        }
    }
}
