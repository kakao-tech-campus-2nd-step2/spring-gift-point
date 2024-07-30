package gift.domain.product.entity;

import gift.exception.DuplicateOptionNameException;
import gift.exception.InvalidProductInfoException;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    private static final int PRODUCT_NAME_MAX_LENGTH = 15;
    private static final String PRODUCT_NAME_REGEXP = "[a-zA-z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]+";
    private static final String IMAGE_URL_REGEXP = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\[\\]\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\[\\]\\+.~#()?&//=]*)";

    @PrePersist
    public void prePersist() {
        validateCategory();
        validateName();
        validatePrice();
        validateImageUrl();
        validateOptions();
    }

    @PreUpdate
    public void preUpdate() {
        validateCategory();
        validateName();
        validatePrice();
        validateImageUrl();
        validateOptions();
    }

    protected Product() {

    }

    public Product(Long id, Category category, String name, int price, String imageUrl) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validateCategory() {
        if (category == null) {
            throw new InvalidProductInfoException("error.invalid.product.category");
        }
    }

    private void validateName() {
        if (!Pattern.matches(PRODUCT_NAME_REGEXP, name) || name.length() > PRODUCT_NAME_MAX_LENGTH || name.contains("카카오")) {
            throw new InvalidProductInfoException("error.invalid.product.name");
        }
    }

    private void validatePrice() {
        if (price < 1 || price > Integer.MAX_VALUE) {
            throw new InvalidProductInfoException("error.invalid.product.price");
        }
    }

    private void validateImageUrl() {
        if (!Pattern.matches(IMAGE_URL_REGEXP, imageUrl)) {
            throw new InvalidProductInfoException("error.invalid.product.imageUrl");
        }
    }

    private void validateOptions() {
        long uniqueOptionNameCount = options.stream().map(Option::getName).distinct().count();
        boolean hasDuplicateOptionName = uniqueOptionNameCount != options.size();

        if ((options.size() == 0) || hasDuplicateOptionName) {
            throw new InvalidProductInfoException("error.invalid.product.options");
        }
    }

    private void validateOption(Option option) {
        options.stream()
            .filter(existingOption -> existingOption.getName().equals(option.getName()))
            .findAny()
            .ifPresent(sameNameOption -> {
                throw new DuplicateOptionNameException("error.duplicate.option.name");
            });
    }

    public boolean hasOption(long optionId) {
        return options.stream()
            .filter(option -> option.getId() == optionId)
            .findAny()
            .isPresent();
    }

    public void addOption(Option option) {
        validateOption(option);
        options.add(option);
        option.setProduct(this);
    }

    public void removeOptions() {
        options.clear();
    }

    public void updateInfo(Category category, String name, int price, String imageUrl) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
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

    public List<Option> getOptions() {
        return options;
    }
}
