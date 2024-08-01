package gift.category.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    private String imageUrl;

    public Category(Long categoryId, String name, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Category(String name, String imageUrl) {
        this(null, name, imageUrl);
    }

    protected Category() {

    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void updateCategory(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
