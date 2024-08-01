package gift.domain.product;

import gift.domain.category.Category;
import gift.domain.option.Option;
import gift.domain.wish.Wish;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Wish> wishList;

    @OneToMany(mappedBy = "product")
    private List<Option> optionList = new ArrayList<>();

    public Product(String name, Long price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    protected Product() {

    }

    public void updateProduct(String name, Long price, String imageUrl, Category category) {
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

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Wish> getWishList() {
        return wishList;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public Category getCategory() {
        return category;
    }

    public void addOption(Option option) {
        optionList.add(option);
        option.setProduct(this);
    }

    public void addWish(Wish wish) {
        wishList.add(wish);
        wish.setProduct(this);
    }
}
