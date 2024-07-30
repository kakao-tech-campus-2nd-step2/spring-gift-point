package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Schema(description = "상품")
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "상품 고유 id")
    private Long id;
    @Column(nullable = false, length = 15)
    @Schema(description = "상품 이름")
    private String name;
    @Column(nullable = false)
    @Schema(description = "상품 가격")
    private Integer price;
    @Column(nullable = false)
    @Schema(description = "상품 url")
    private String url;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "상품이 속한 카테고리")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //상품에 어떤 옵션들이 있는지 보려면 필요할듯
    @Schema(description = "상품이 갖고 있는 옵션들")
    private List<Option> options = new ArrayList<>();

    public Product() {
    }

    public Product(String name, Integer price, String url, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

    public Product(Long id, String name, Integer price, String url, Category category, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

    public Product(String name, Integer price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(String name, Integer price, String url, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

}
