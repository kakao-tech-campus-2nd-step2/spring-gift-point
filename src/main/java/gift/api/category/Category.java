package gift.api.category;

import gift.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_category", columnNames = {"name"}))
public class Category extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(length = 7, nullable = false)
    private String color;
    @Column(nullable = false)
    private String imageUrl;
    private String description;

    protected Category(){
    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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
}
