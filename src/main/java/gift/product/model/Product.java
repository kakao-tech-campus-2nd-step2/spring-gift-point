package gift.product.model;

import gift.category.model.Category;
import gift.option.domain.Option;
import gift.wish.model.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Price는 null일 수 없습니다.")
    @Range(min = 0, message = "Price는 0 이상이어야 합니다.")
    private Integer price;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // 생성자
    public Product() {}
    public Product(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, Integer price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // 활용 메서드들
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product comparingProduct)) return false;
        return price == comparingProduct.price &&
                name.equals(comparingProduct.name) &&
                imageUrl.equals(comparingProduct.imageUrl);
    }

    public void addOption(Option option) {
        this.options.add(option);
        option.setProduct(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setProduct(null);
    }

    public void addWish(Wish wish) {
        this.wishes.add(wish);
        wish.setProduct(this);
    }

    public void removeWish(Wish wish) {
        wishes.remove(wish);
        wish.setProduct(null);
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
