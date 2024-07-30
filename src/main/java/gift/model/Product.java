package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 15, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private String price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    protected Product() {
    }

    public Product(Long id, String name, String price, Category category, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void updateProduct(String name, String price, Category category, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 최소 1자 이상이어야 합니다.");
        }
        if (name.length() > 15) {
            throw new IllegalArgumentException("상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.");
        }
        if (!name.matches("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$")) {
            throw new IllegalArgumentException("상품 이름에 (), [], +, -, &, /, _ 외 특수 문자는 사용할 수 없습니다.");
        }
        if (name.contains("카카오")) {
            throw new IllegalArgumentException("'카카오'가 포함된 문구는 담당 MD와 협의 후 사용 바랍니다.");
        }
    }

    private void validatePrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            throw new IllegalArgumentException("가격을 입력해야 합니다.");
        }
        if (!price.matches("^\\d+$")) {
            throw new IllegalArgumentException("가격은 0이상의 숫자만 입력 가능합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("이미지 URL을 입력해야 합니다.");
        }
        if (!imageUrl.matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("유효한 이미지 URL을 입력해야 합니다.");
        }
    }
}