package gift.entity;

import gift.exception.OptionDuplicateException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Option> options = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_category_id_ref_category_id"))
    private Category category;

    protected Product() {
    }

    public Product(String name, Integer price, String imageUrl, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options.addAll(options);
        for (Option option : options) {
            option.associateWithProduct(this);
        }
    }

    public void addOption(Option option) {
        checkDuplicateOptionName(option.getName());

        options.add(option);
        option.associateWithProduct(this);
    }

    public Long getId() {
        return id;
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

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void checkDuplicateOptionName(String newOptionName) {
        boolean isDuplicate = options.stream()
                .anyMatch(option -> option.isSameName(newOptionName));

        if (isDuplicate) {
            throw new OptionDuplicateException(newOptionName);
        }
    }
}
