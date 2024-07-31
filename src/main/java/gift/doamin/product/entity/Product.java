package gift.doamin.product.entity;

import gift.doamin.category.entity.Category;
import gift.doamin.product.dto.ProductForm;
import gift.doamin.user.entity.User;
import gift.global.AuditingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();

    public Product(User user, Category category, String name, Integer price, String imageUrl) {
        this.user = user;
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    protected Product() {

    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void updateAll(ProductForm productForm, Category category) {
        this.category = category;
        this.name = productForm.getName();
        this.price = productForm.getPrice();
        this.imageUrl = productForm.getImageUrl();
    }

    public void addOption(Option option) {
        this.options.add(option);
        option.setProduct(this);
    }
}
