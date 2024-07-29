package gift.product.entity;

import gift.product.dto.ProductRequest;
import gift.wishlist.entity.Wish;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private final List<Wish> wishList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Option> options = new HashSet<>();

    protected Product() {
    }

    public Product(String name,
                   int price,
                   String imageUrl,
                   Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(ProductBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.category = builder.category;
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

    public Set<Option> getOptions() {
        return options;
    }

    public void update(ProductRequest request, Category category) {
        this.name = request.name();
        this.price = request.price();
        this.imageUrl = request.imageUrl();
        this.category = category;
    }

    /**
     * Option Set에 주어진 option을 추가하는 메서드
     * Set option 추가 여부를 반환
     *
     * @param option Set에 추가되려는 option
     * @return option이 Set에 성공적으로 추가되면 true 반환
     *         Set에 option이 이미 존재하여 추가되지 않으면 false 반환
     */
    public boolean addOptionOrElseFalse(Option option) {
        return this.options
                   .add(option);
    }

    public void deleteOption(Option option) {
        this.options
            .remove(option);
    }

    public boolean hasOnlyOneOption() {
        return this.options
                   .size() == 1;
    }

    public static class ProductBuilder {
        private String name;
        private int price;
        private String imageUrl;
        private Category category;

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setPrice(int price) {
            this.price = price;
            return this;
        }

        public ProductBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductBuilder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Product build() {
            return new Product(this);
        }

    }

}
