package gift.model;


import gift.exception.InputException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_category_id_ref_category_id"))
    private Category category;

    protected Product() {
    }

    public Product(Long id, String name, Integer price, String imageUrl, Category category) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        validateCategory(category);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, Integer price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, Integer price, String imageUrl, Category category) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        validateCategory(category);
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

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category.getName();
    }

    public void updateProduct(String newName, Integer newPrice, String newImageUrl, String newCategoryName) {
        validateName(newName);
        validatePrice(newPrice);
        validateImageUrl(newImageUrl);
        this.name = newName;
        this.price = newPrice;
        this.imageUrl = newImageUrl;
        category.updateCategory(newCategoryName);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 15) {
            throw new InputException("1~15자 사이로 입력해주세요.");
        }
        if (!name.matches("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9()+&/_ \\[\\]-]*$")) {
            throw new InputException("상품명에 특수 문자는 '(, ), [, ], +, -, &, /, -' 만 입력 가능합니다.");
        }
    }

    private void validatePrice(Integer price) {
        if (price == null) {
            throw new InputException("가격을 입력해주세요.");
        }
        if (price < 100 || price > 1000000) {
            throw new InputException("상품 가격은 100원~1,000,000원 사이여야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new InputException("이미지 주소를 입력해주세요.");
        }
        if (!imageUrl.matches("^(https?)://[^ /$.?#].[^ ]*$")) {
            throw new InputException("올바른 url이 아닙니다.");
        }
    }

    private void validateCategory(Category category) {
        if (category == null || category.getId() == null) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }
}
