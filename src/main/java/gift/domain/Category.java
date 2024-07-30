package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "category", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryName name;

    @Column
    private String color;

    @Column
    private String imageUrl;

    @Column
    private String description;

    protected Category() {
    }

    private Category(CategoryBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
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

    public static class CategoryBuilder {
        private Long id;
        private CategoryName name;
        private String color;
        private String imageUrl;
        private String description;

        public CategoryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder name(CategoryName name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder color(String color) {
            this.color = color;
            return this;
        }

        public CategoryBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CategoryBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
