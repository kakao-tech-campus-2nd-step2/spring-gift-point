package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(max=255)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @Column(name = "color" ,nullable = false)
    @Size(max = 7)
    private String color;

    @Column(name = "description")
    @Size(max = 255)
    private String description;

    @Column(name = "image_url",nullable = false)
    @Size(max = 255)
    private String imageUrl;

    public Category() {
    }

    public Category(String name, String color, String description, String imageUrl) {
        this.name =name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Category(Long id, String name, String color, String description,
        String imageUrl) {
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
}
