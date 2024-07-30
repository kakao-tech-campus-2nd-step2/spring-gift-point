package gift.product.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    private String name;

    private String description;

    private String imageUrl;

    private String color;


    public Category() {
    }

    public Category(CreateCategoryRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.imageUrl = request.getImageUrl();
        this.color = request.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
