package gift.category.model;

import gift.product.model.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();



    // 활용 메서드들
    public void addProduct(Product product) {
        this.products.add(product);
        product.setCategory(this);
    }

    public void removeWish(Product product) {
        products.remove(product);
        product.setCategory(null);
    }

    // 생성자
    public Category() {

    }
    public Category(String name) {
        this.name = name;
    }

    // getter & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
