package gift.product.option.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.entity.Product;
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
import jakarta.validation.constraints.NotNull;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

@Entity
@Table(name = "options")
public class Option {

    private static final Pattern OPTION_NAME_PATTERN = Pattern.compile(
        "^[\\w\\s()\\[\\]+\\-&/_]*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Column
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_options_product_id_ref_products_id"))
    private Product product;

    public Option(String name, Integer quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    protected Option() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void initProduct(Product product) {
        Assert.notNull(product, "product is null");
        this.product = product;
    }

    public void edit(String name, Integer quantity) {
        validateName(name);
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(Integer subtractionQuantity) {
        if (subtractionQuantity < 1 || quantity < subtractionQuantity) {
            throw new CustomException(ErrorCode.INVALID_OPTION_QUANTITY);
        }
        this.quantity -= subtractionQuantity;
    }

    private void validateName(String name) {
        if (!OPTION_NAME_PATTERN.matcher(name).matches() || name.length() > 50) {
            throw new CustomException(ErrorCode.INVALID_OPTION_NAME);
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new CustomException(ErrorCode.INVALID_OPTION_QUANTITY);
        }
    }

}
