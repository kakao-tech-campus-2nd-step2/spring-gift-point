package gift.category.entity;

import gift.category.dto.CategoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Column(name = "description", nullable = false)
    private String description;

    protected Category() {

    }

    public Category(Long id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(String name, String imageUrl, String description) {
        this(null, name, imageUrl, description);
    }

    public CategoryDto toDto() {
        return new CategoryDto(id, name, imageUrl, description);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
