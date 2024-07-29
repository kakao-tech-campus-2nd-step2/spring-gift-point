package gift.administrator.product;

import gift.administrator.category.Category;
import gift.administrator.option.Option;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false, unique = true, length = 15)
    private String name;
    @Column(nullable = false)
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, Category category,
        List<Option> options) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public Product(String name, int price, String imageUrl, Category category,
        List<Option> options) {
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public void update(String name, int price, String imageUrl, Category category) {
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setOption(List<Option> options) {
        this.options = options;
    }

    public void addOption(Option option) {
        options.add(option);
        option.setProduct(this);
    }

    public void addOptions(List<Option> options) {
        for (Option option : options) {
            addOption(option);
        }
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setProduct(null);
    }

    public void removeOptions(List<Option> options) {
        for (Option option : options) {
            removeOption(option);
        }
    }

    public List<Option> getOptions() {
        return options;
    }
}
