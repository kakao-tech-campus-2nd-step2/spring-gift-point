package gift.model;

import gift.common.exception.ErrorCode;
import gift.common.exception.OptionException;
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
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 30)
    private String name;

    @NotNull
    private int price;

    @NotNull
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {
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

    public List<Option> getOptions() {
        return options;
    }

    public void updateProduct(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void checkDuplicateName(String name) {
        boolean duplicate = options.stream()
            .anyMatch(option -> option.isDuplicateName(name));

        if (duplicate) {
            throw new OptionException(ErrorCode.DUPLICATE_OPTION_NAME);
        }
    }

    public boolean hasOneOption() {
        return this.options.size() == 1;
    }

    public void addOption(Option option) {
        this.options.add(option);
        option.addProduct(this);
    }

    public void updateOption(Option updateOption) {
        int index = options.stream()
            .filter(option -> option.getName().equals(updateOption.getName()))
            .map(options::indexOf)
            .findFirst().get();

        options.set(index, updateOption);
    }

    public void removeOption(Option option) {
        this.options.remove(option);
    }
}
