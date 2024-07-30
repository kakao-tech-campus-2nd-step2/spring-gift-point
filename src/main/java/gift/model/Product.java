package gift.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '상품 ID'")
    private Long id;

    @NotNull(message = "이름을 입력해주세요.")
    @Length(min = 1, max = 15, message = "1자 ~ 15자까지 가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 현재 사용 할 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]*$", message = "사용불가한 특수 문자가 포함되어 있습니다.")
    private String name;

    @Column(nullable = false, columnDefinition = "INTEGER COMMENT '상품 가격'")
    private int price;

    @Column(name = "image_url", nullable = false, columnDefinition = "VARCHAR(255) COMMENT '상품 이미지 URL'")
    private String imageUrl;

    @Column(name = "category_id", nullable = false, columnDefinition = "BIGINT COMMENT '카테고리 ID'")
    private Long categoryId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {}

    public Product(Long id, String name, int price, String imageUrl, Long categoryId, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
        for (Option option : options) {
            option.assignProduct(this);
        }
    }
    // Option management methods
    public void addOption(Option option) {
        options.add(option);
        option.assignProduct(this);
    }

    public void clearOptions() {
        for (Option option : options) {
            option.removeProduct();
        }
        options.clear();
    }

    public void update(String name, int price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    // Getters
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

    public Long getCategoryId() {
        return categoryId;
    }

    public List<Option> getOptions() {
        return options;
    }


}