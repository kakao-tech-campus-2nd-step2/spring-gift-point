package gift.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String imageUrl;

    @Column
    private String description;

    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    private Product(ProductBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
        this.wishes = builder.wishes;
        this.options = builder.options != null ? builder.options : new ArrayList<>();
        this.category = builder.category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private String imageUrl;
        private String description;
        private List<Option> options;
        private List<Wish> wishes;
        private Category category;

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder wishes(List<Wish> wishes) {
            this.wishes = wishes;
            return this;
        }

        public ProductBuilder options(List<Option> options) {
            this.options = options;
            return this;
        }


        public ProductBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public ProductBuilder toBuilder() {
        return new ProductBuilder()
            .id(this.id)
            .name(this.name)
            .price(this.price)
            .imageUrl(this.imageUrl)
            .description(this.description)
            .wishes(this.wishes)
            .options(this.options)
            .category(this.category);
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}
