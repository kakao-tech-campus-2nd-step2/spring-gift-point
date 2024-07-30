package gift.domain;

import gift.dto.request.ProductRequestDto;
import gift.utils.TimeStamp;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private int price;
    private String imageUrl;
    @OneToMany(mappedBy = "product")
    private List<Wish> wishList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Option> options = new ArrayList<>();

    public Product() {
    }

    private Product(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.category = builder.category;
    }

    public static class Builder {
        private String name;
        private int price;
        private String imageUrl;

        private Category category;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
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

    public List<Wish> getWishList() {
        return wishList;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(ProductRequestDto productDto, Category category){
        this.name = productDto.name();
        this.price = productDto.price();
        this.imageUrl = productDto.imageUrl();
        this.category = category;
    }

    public void addCategory(Category category){
        this.category = category;
        category.getProducts().add(this);
    }
}
