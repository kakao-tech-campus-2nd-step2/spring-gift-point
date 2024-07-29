package gift.domain.product.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import gift.domain.category.entity.Category;
import gift.domain.option.entity.Option;
import gift.domain.wishlist.entity.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    protected Product() {
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this(null, name, price, imageUrl, category);
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void updateAll(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }
}
