package gift.product.entity;

import gift.product.dto.CategoryRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false, name = "image_url")
    private String imageUrl;
    private String description;

    protected Category() {
    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(CategoryBuilder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
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

    public void update(CategoryRequest request) {
        this.name = request.name();
        this.color = request.color();
        this.imageUrl = request.imageUrl();
        this.description = request.description();
    }

    public static class CategoryBuilder {
        private String name;
        private String color;
        private String imageUrl;
        private String description;

        public CategoryBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public CategoryBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CategoryBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            return new Category(this);
        }

    }

}
