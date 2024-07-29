package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "상품을 입력해주세요")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$", message = "특수문자는 (),[],+,-,&,/,_만 가능합니다")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "상품 이름에 '카카오'를 포함할 수 없습니다. 관리자와의 협의가 필요합니다.")
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String imageurl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id")
    private Long categoryId;

    @OneToMany(mappedBy = "product")
    private List<Wish> wishes;

    @OneToMany(mappedBy = "product")
    private List<ProductOption> options = new ArrayList<>();

    public Product() {
    }

    public Product(String name, Integer price, String imageurl, Category category) {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        setCategory(category);
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

    public String getImageurl() {
        return imageurl;
    }

    public Category getCategory() {
        return category;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public List<ProductOption> getOptions() {
        return options;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category != null ? category.getId() : null;
    }

    public void updateProductDetails(String name, Integer price, String imageurl, Category category, List<ProductOption> options) {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        setCategory(category);
        setOptions(options);
    }

    private void setOptions(List<ProductOption> options) {
        this.options = options;
        if (options != null) {
            for (ProductOption option : options) {
                option.setProduct(this);
            }
        }
    }
}
