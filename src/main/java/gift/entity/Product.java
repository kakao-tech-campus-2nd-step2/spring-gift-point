package gift.entity;

import groovy.transform.builder.Builder;
import jakarta.persistence.*;

@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "imageURL", nullable = false)
    private String imageURL;

    protected Product() {}

    public Product(Category category, int price, String name, String imageURL) {
        this.category = category;
        this.price = price;
        this.name = name;
        this.imageURL = imageURL;
    }

    public Product(int id, Category category, int price, String name, String imageURL) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getIdAsString() {
        return String.valueOf(id);
    }

    public int getId() { return id; }
    public double getPrice() { return price;}
    public String getName() { return name;}
    public String getImageURL() { return imageURL;}
    public int getCategoryId() { return category.getId(); }
}