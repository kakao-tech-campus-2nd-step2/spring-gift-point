package gift.product.entity;

import gift.category.entity.Category;
import gift.option.entity.Option;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    // 처음에는 id로 설정했다가 헷갈려서 productId로 쓰기로 하였습니다..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Embedded
    private Options options;

    protected Product() {
    }

    public Product(Long productId, String name, int price, String imageUrl, Category category,
        Options options) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public static Product of(String name, int price, String imageUrl, Category category,
        Option option) {
        // 상품 추가 시에 무조건 하나의 옵션을 넣게 해서 최소 하나 이상의 옵션 유지
        return new Product(null, name, price, imageUrl, category, new Options(option));
    }

    public void updateProduct(String name, int price, String image, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = image;
        this.category = category;
    }

    public Long getProductId() {
        return productId;
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

    public Options getOptions() {
        return options;
    }

    public void addNewOption(Option option) {
        options.addNewOption(option, productId);
    }
}
