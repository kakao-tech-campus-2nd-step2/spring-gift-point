package gift.product.persistence.entity;

import gift.global.domain.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "product",
    indexes = {@Index(name = "idx_name", columnList = "name"), @Index(name = "idx_price", columnList = "price")}
)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Integer price;
    @NotNull
    private String url;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @BatchSize(size = 20)
    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Option> options = new ArrayList<>();

    public Product(String name, String description, Integer price, String url, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
        this.category = category;
    }

    protected Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void update(String name, String description, Integer price, String url) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOptions(List<Option> options) {
        Set<String> optionNames = this.options.stream()
            .map(Option::getName)
            .collect(Collectors.toSet());
        optionNames.addAll(options.stream()
            .map(Option::getName)
            .collect(Collectors.toSet()));

        if (optionNames.size() != this.options.size() + options.size()) {
            throw new IllegalArgumentException("Option names are duplicated");
        }

        options.forEach(option -> option.setProduct(this));
        this.options.addAll(options);
    }

    public void updateOptions(Map<Long, Option> optionMap) {
        Set<String> optionNames = optionMap.values().stream()
            .map(Option::getName)
            .collect(Collectors.toSet());
        if (optionNames.size() != optionMap.size()) {
            throw new IllegalArgumentException("Option names are duplicated");
        }

        options.stream()
            .filter(option -> optionMap.containsKey(option.getId()))
            .forEach(option -> {
                var updatedOption = optionMap.get(option.getId());
                option.update(updatedOption.getName(), updatedOption.getQuantity());
            });
    }

    public void deleteOptions(List<Long> optionIds) {
        options.removeIf(option -> optionIds.contains(option.getId()));
    }

    public void subtractOption(Long id, Integer quantity) {
        var option = options.stream()
            .filter(o -> o.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Option not found"));
        option.subtractQuantity(quantity);
    }

    public void updateOption(Long id, String name, Integer quantity) {
        var option = options.stream()
            .filter(o -> o.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Option not found"));
        option.update(name, quantity);
    }

    public Option getOptionByOptionId(Long optionId) {
        return options.stream()
            .filter(option -> option.getId().equals(optionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Option not found"));
    }
}
