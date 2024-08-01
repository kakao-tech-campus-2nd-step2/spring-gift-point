package gift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gift.dto.request.CategoryRequestDTO;
import gift.dto.request.ProductRequestDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name="name")
    private String name;

    @Column(nullable = false, name="color")
    private String color;

    @Column(name="description")
    private String description;

    @Column(nullable = false, name="image_url")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "category", orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products = new ArrayList<Product>();

    public Category(){

    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void updateCategory(CategoryRequestDTO categoryRequestDTO){
        this.name = categoryRequestDTO.name();
        this.color = categoryRequestDTO.color();
        this.description = categoryRequestDTO.description();
        this.imageUrl = categoryRequestDTO.imageUrl();
    }

    public void addProduct(Product product){
        this.products.add(product);
        //product.setCategory(this);
    }

    public void addProduct(ProductRequestDTO productRequestDTO){
        Product product = new Product(productRequestDTO.name(),
                productRequestDTO.price(),
                productRequestDTO.imageUrl(),
                this);
        this.products.add(product);
    }


}
