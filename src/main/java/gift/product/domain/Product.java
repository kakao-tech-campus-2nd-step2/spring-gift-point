package gift.product.domain;

import gift.product.exception.product.ProductNoConferredException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.annotations.SoftDelete;

@Entity
@SoftDelete
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Product() {
    }

    public Product(String name, Integer price, String imgUrl, Category category) {
        this(null, name, price, imgUrl, category);
    }

    public Product(Long id, String name, Integer price, String imgUrl, Category category) {
        checkName(name);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void modify(String name, Integer price, String imgUrl, Category category) {
        checkName(name);
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    private void checkName(String name) {
        if (name.contains("카카오")) {
            throw new ProductNoConferredException(List.of("카카오"));
        }
    }
}
