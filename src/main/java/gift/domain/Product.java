package gift.domain;

import gift.exception.errorMessage.Messages;
import gift.exception.customException.ProductOptionRequiredException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable = false, length = 15)
    private String name;
    @Column(name="price", nullable = false)
    private int price;
    @Column(name="image_url", nullable = false)
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public void addOption(Option option) {
        this.options.add(option);
        option.setProduct(this);
    }

    public void removeOption(Option option) {
        option.setProduct(null);
        this.options.remove(option);
    }

    public void removeOptions() {
        Iterator<Option> iterator = options.iterator();

        while(iterator.hasNext()){
            Option option = iterator.next();
            option.setProduct(null);
            iterator.remove();
        }
    }

    public void addWish(Wish wish) {
        this.wishes.add(wish);
        wish.setProduct(this);
    }

    public void removeWish(Wish wish) {
        wish.setProduct(null);
        this.wishes.remove(wish);
    }

    public void removeWishes() {
        Iterator<Wish> iterator = wishes.iterator();

        while(iterator.hasNext()) {
            Wish wish = iterator.next();
            wish.setProduct(null);
            iterator.remove();
        }
    }

    protected Product () {
    }

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.category = builder.category;
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, int price, String imageUrl, Category category, Option option){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        if(this.category != null){
            this.category.addProduct(this);
        }
        addOption(option);
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void updateProduct(String name, int price, String imageUrl, Category category){
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void remove(){
        if(this.category != null){
            this.category.removeProduct(this);
        }
    }

    @PrePersist
    @PreUpdate
    private void isOptionPresent() {
        if (this.options.isEmpty()) {
            throw new ProductOptionRequiredException(Messages.PRODUCT_OPTION_REQUIRED);
        }
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
            Product product = new Product(this);
            if(product.category != null && product.id == null){
                product.category.addProduct(product);
            }
            return product;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
