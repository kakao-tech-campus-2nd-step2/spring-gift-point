package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Category extends BasicEntity{
    @Column(unique=true)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false, length = 1000)
    private String imageUrl;
    @Column()
    private String description;

    protected Category() {}

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void updateCategory(String name, String color, String imageUrl, String description) {
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
