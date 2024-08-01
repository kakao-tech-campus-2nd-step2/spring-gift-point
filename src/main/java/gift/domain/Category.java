package gift.domain;

import gift.dto.CategoryDto;
import gift.dto.RequestCategoryDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column
    private String description;

    protected Category() {

    }

    public Category(String name, String color, String imageUrl, String description) {
        this.id = null;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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

    public CategoryDto toCategoryDto() {
        return new CategoryDto(this.id, this.name, this.color, this.imageUrl, this.description);
    }

    public void update(RequestCategoryDto requestCategoryDto) {
        this.name = requestCategoryDto.getName();
        this.color = requestCategoryDto.getColor();
        this.imageUrl = requestCategoryDto.getImageUrl();
        this.description = requestCategoryDto.getDescription();
    }
}
