package gift.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_category_id_ref_category_id"))
    Category category;

    @NotBlank
    @Size(max = 15)
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer price;

    @NotBlank
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    public Product() {}

    public Product(Category category, String name, int price, String imageUrl) {
        this(null, category, name, price, imageUrl);
    }

    public Product(Long id, Category category, String name, int price, String imageUrl) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;

        validateName();
        validateCategoryExist();
    }

    private void validateName() {
        if (name == null || name.length() > 15) {
            throw new IllegalArgumentException("상품명은 15자를 넘을 수 없습니다.");
        }

        if (!name.matches("^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/]*$")) {
            throw new IllegalArgumentException("상품명에 () [] + - & / 외의 특수기호는 불가합니다");
        }

        if (name.contains("카카오")) {
            throw new IllegalArgumentException("`카카오`가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다");
        }
    }

    private void validateCategoryExist() {
        if (category == null) {
            throw new IllegalArgumentException("상품에는 카테고리가 존재해야합니다.");
        }
    }

    public static void validateOptionsExist(List<Option> options) {
        if (options.isEmpty()) {
            throw new IllegalArgumentException("상품에는 하나 이상의 옵션이 필요합니다.");
        }
    }

    public void addOption(Option option) {
        options.add(option);
        option.setProduct(this);
    }

    public void removeOptions() {
        options.clear();
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
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

    public List<Option> getOptions() {
        return options;
    }
}
