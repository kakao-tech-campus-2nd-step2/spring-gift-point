package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category extends BaseEntity{
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "color", nullable = false)
    String color;
    @Column(name = "imageUrl", nullable = false)
    String imageUrl;

    protected Category() {
        super();
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void update(String name, String description, String color, String imageUrl){
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }
}
