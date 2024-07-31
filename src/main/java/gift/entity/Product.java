package gift.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "price", nullable = false, columnDefinition = "INTEGER")
    private Integer price;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(15)")
    private String name;

    @Column(name = "img_url", nullable = false, columnDefinition = "VARCHAR(255)"   )
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> options = new HashSet<>();

    protected Product() {}

    public Product(String name, Integer price, String imgUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public void update(String name, Integer price, String imgUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public void addOption(Option option) {
        options.add(option);
        option.assignToProduct(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.removeFromProduct();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Category getCategory() {
        return category;
    }

    public Set<Option> getOptions() {
        return options;
    }
}