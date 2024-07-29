package gift.product;

import gift.category.Category;
import gift.option.Option;
import gift.wishList.WishList;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Long price;
    @Column(name = "imageUrl")
    private String imageUrl;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product", orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product", orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void addOptions(Option option) {
        this.options.add(option);
        option.setProduct(this);
    }

    public void removeOption(Option option) {
        option.setProduct(null);
        this.options.remove(option);
    }

    public void removeOptions() {
        for (Option option : options) {
            removeOption(option);
        }
    }

    public void addWishList(WishList wishList) {
        this.wishLists.add(wishList);
        wishList.setProduct(this);
    }

    public void removeWishList(WishList wishList) {
        wishList.setProduct(null);
        this.wishLists.remove(wishList);
    }

    public void removeWishLists() {
        for (WishList wishList : wishLists) {
            removeWishList(wishList);
        }
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product(String name, Long price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
