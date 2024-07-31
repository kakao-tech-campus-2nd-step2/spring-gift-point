package gift.product.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.category.entity.Category;
import gift.product.dto.request.UpdateProductRequest;
import gift.product.option.entity.Option;
import gift.product.option.entity.Options;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.util.Assert;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_products_category_id_ref_categories_id"))
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Option> optionSet = new HashSet<>();

    @Transient
    private Options options;

    public Product(String name, Integer price, String imageUrl, Category category,
        Set<Option> optionSet) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.optionSet = new HashSet<>(optionSet);
        this.options = new Options(this.optionSet);
    }

    public Product(String name, Integer price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.optionSet = new HashSet<>();
        this.options = new Options(this.optionSet);
    }

    protected Product() {
    }

    public void edit(UpdateProductRequest request, Category category) {
        this.name = request.name();
        this.price = request.price();
        this.imageUrl = request.imageUrl();
        this.category = category;
    }

    public void addOption(Option option) {
        Assert.notNull(option, "Option is null");
        option.initProduct(this);
        this.options.addOption(option);
        this.optionSet.add(option);
    }

    public void editOption(Long optionId, String name, Integer quantity) {
        Option option = this.optionSet.stream()
            .filter(option1 -> option1.getId().equals(optionId))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        Option temp = new Option(name, quantity, this);
        this.options.validateNameDuplicate(temp);
        option.edit(name, quantity);
    }

    public void removeOption(Option option) {
        options.removeOption(option);
        optionSet.remove(option);
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

    public String getCategoryName() {
        return category.getName();
    }

    public Options getOptions() {
        return options;
    }

    public void changeName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @PostLoad
    private void postLoad() {
        this.options = new Options(this.optionSet);
    }

}
