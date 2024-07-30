package gift.main.entity;

import gift.main.dto.CategoryRequest;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, unique = true)
    private String name;


    public Category() {

    }

    public Category(CategoryRequest categoryRequest) {
        this.color = categoryRequest.color();
        this.imageUrl = categoryRequest.imageUrl();
        this.description = categoryRequest.description();
        this.name = categoryRequest.name();
    }

    public void updateCategory(CategoryRequest categoryRequest) {
        this.color = categoryRequest.color();
        this.imageUrl = categoryRequest.imageUrl();
        this.description = categoryRequest.description();
        this.name = categoryRequest.name();
    }

    public long getId() {
        return id;
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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
