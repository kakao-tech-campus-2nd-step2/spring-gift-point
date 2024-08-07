    package gift.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Positive;
    import jakarta.validation.constraints.Size;
    import lombok.Getter;

    import java.util.ArrayList;
    import java.util.List;

    @Getter
    @Entity
    public class Product {
    @Positive(message = "price must be positive")
    @Column(nullable = false)
    private int price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be blank")
    @Size(max = 15, message = "name must be less than 15 characters")
    @Column(nullable = false, length = 15)
    private String name;

    @NotBlank(message = "image url must not be blank")
    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.category = builder.category;
    }

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void update(int price, String name, String imageUrl, Category category) {
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void updateId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public static class Builder {
       private Long id;
       private String name;
       private int price;
       private String imageUrl;
       private Category category;

       public Builder id(Long id) {
           this.id = id;
           return this;
       }
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
}
