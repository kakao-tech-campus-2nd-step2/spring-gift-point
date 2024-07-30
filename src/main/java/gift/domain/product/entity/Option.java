package gift.domain.product.entity;

import gift.exception.InvalidOptionInfoException;
import gift.exception.OutOfStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.util.regex.Pattern;

@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "product_id", "name" }) }
)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Version
    private Long version;

    private static final int OPTION_NAME_MAX_LENGTH = 50;
    private static final int OPTION_QUANTITY_MIN = 0;
    private static final int OPTION_QUANTITY_MAX = 100000000;
    private static final String OPTION_NAME_REGEXP = "[a-zA-z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]+";

    @PrePersist
    public void prePersist() {
        validateProduct();
        validateName();
        validateQuantity();
    }

    @PreUpdate
    public void preUpdate() {
        validateProduct();
        validateName();
        validateQuantity();
    }

    private void validateProduct() {
        if (product == null) {
            throw new InvalidOptionInfoException("error.invalid.option.product");
        }
    }

    private void validateName() {
        if (!Pattern.matches(OPTION_NAME_REGEXP, name) || name.length() > OPTION_NAME_MAX_LENGTH) {
            throw new InvalidOptionInfoException("error.invalid.option.name");
        }
    }

    private void validateQuantity() {
        if (quantity < OPTION_QUANTITY_MIN || quantity > OPTION_QUANTITY_MAX) {
            throw new InvalidOptionInfoException("error.invalid.option.quantity");
        }
    }

    protected Option() {
    }

    public Option(Long id, Product product, String name, int quantity) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new OutOfStockException("error.option.out.of.stock");
        }
        this.quantity -= quantity;
    }
}
