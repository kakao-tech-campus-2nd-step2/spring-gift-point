package gift.entity;

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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String description;

    protected Category() {}

    public Category(String name, String imageUrl, String color, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.color = color;
        this.description = description;
    }

    public Category(Long id, String name, String imageUrl, String color, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.color = color;
        this.description = description;
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

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }
}