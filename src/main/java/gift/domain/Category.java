package gift.domain;

import gift.dto.request.CategoryRequestDto;
import gift.utils.TimeStamp;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    String imageUrl;

    String description;

    public Category() {
    }

    public Category(String name, String color, String imageUrl) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

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

    public List<Product> getProducts() {
        return products;
    }

    public void update(CategoryRequestDto categoryRequestDto){
        this.name = categoryRequestDto.name();
        this.color = categoryRequestDto.color();
    }
}
