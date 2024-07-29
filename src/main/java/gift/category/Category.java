package gift.category;

import gift.product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;
    @Column(name = "imageUrl")
    private String imageUrl;
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "category", orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product){
        product.setCategory(null);
        this.products.remove(product);
    }

    public void updateWithRequest(CategoryRequest updateParam){
        this.name = updateParam.name();
        this.color = updateParam.color();
        this.imageUrl = updateParam.imageUrl();
        this.description = updateParam.description();
    }

    public void removeProducts(){
        for (Product product : products) {
            removeProduct(product);
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
