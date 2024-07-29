package gift.category.domain;

import gift.category.application.command.CategoryUpdateCommand;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "category", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    protected Category() {
    }

    public Category(String name, String color, String description, String imageUrl) {
        this(null, name, color, description, imageUrl);
    }

    public Category(Long id, String name, String color, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void update(CategoryUpdateCommand command) {
        this.name = command.name();
        this.color = command.color();
        this.description = command.description();
        this.imageUrl = command.imageUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
