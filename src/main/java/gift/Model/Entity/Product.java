package gift.Model.Entity;

import gift.Model.Value.ImageUrl;
import gift.Model.Value.Name;
import gift.Model.Value.Price;
import jakarta.persistence.*;

import java.awt.*;
import java.util.regex.Pattern;

@Entity
public class Product {

    private static final int NAME_MAX_LENGTH = 15;

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-\\&/_]*$"
    );

    private static final Pattern NAME_EXCLUDE_PATTERN = Pattern.compile(
            "^(?!.*카카오).*$"
    );

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, length = 50))
    private Name name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Price price;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "image_url", nullable = false))
    private ImageUrl imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;

    protected Product() {}

    public Product(Name name, Price price, ImageUrl imageUrl, Category category) {
        name.checkNameLength(NAME_MAX_LENGTH);
        name.checkNamePattern(NAME_PATTERN);
        name.checkNamePattern(NAME_EXCLUDE_PATTERN);

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        Name nameObj = new Name(name);
        nameObj.checkNameLength(NAME_MAX_LENGTH);
        nameObj.checkNamePattern(NAME_PATTERN);
        nameObj.checkNamePattern(NAME_EXCLUDE_PATTERN);

        Price priceObj = new Price(price);
        ImageUrl imageUrlObj = new ImageUrl(imageUrl);

        this.name = nameObj;
        this.price = priceObj;
        this.imageUrl = imageUrlObj;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void update(Name name, Price price, ImageUrl imageUrl, Category category){
        name.checkNameLength(NAME_MAX_LENGTH);
        name.checkNamePattern(NAME_PATTERN);
        name.checkNamePattern(NAME_EXCLUDE_PATTERN);

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void update(String name, int price, String imageUrl, Category category) {
        update (new Name(name), new Price(price), new ImageUrl(imageUrl), category);
    }

    public boolean isSameId(Long productId) {
        return this.id.equals(productId);
    }
}