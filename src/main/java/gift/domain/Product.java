package gift.domain;

import gift.exception.ErrorCode;
import gift.dto.ProductDto;
import gift.exception.GiftException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    public Product() {
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

    public List<Option> getOptions() {
        return options;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDto toDto() {
        return new ProductDto(this.getId(), this.getName(), this.getPrice(),
                this.getImageUrl());
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addOption(Option option) {
        option.setProduct(this);
        options.add(option);
    }

    public void validateOptionNameUnique(String optionName) {
        if (options.stream().anyMatch(option -> option.getName().equals(optionName))) {
            throw new GiftException(ErrorCode.DUPLICATE_OPTION);
        }
    }

    public void removeOptionById(Long optionId) {
        boolean optionExists = options.stream().anyMatch(option -> option.getId().equals(optionId));

        if (!optionExists) {
            throw new GiftException(ErrorCode.OPTION_NOT_FOUND);
        }

        if (options.size() == 1) {
            throw new GiftException(ErrorCode.AT_LEAST_ONE_OPTION_REQUIRED);
        }

        options.removeIf(option -> option.getId().equals(optionId));
    }

    public Option getOptionById(Long optionId) {
        return options.stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new GiftException(ErrorCode.OPTION_NOT_FOUND));
    }

}
