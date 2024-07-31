package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
@Table(name = "product_option", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_option_id", columnList = "option_id")
})
public class ProductOption {
    public static final int MIN_QUANTITY = 0;
    public static final int MAX_QUANTITY = 100000000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_option_product"), columnDefinition = "BIGINT NOT NULL COMMENT 'Foreign Key to Product'")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_option_option"), columnDefinition = "BIGINT NOT NULL COMMENT 'Foreign Key to Option'")
    private Option option;

    @Column(nullable = false, columnDefinition = "INT NOT NULL COMMENT 'Option Quantity'")
    private int quantity;

    @Version
    @Column(nullable = false, columnDefinition = "BIGINT NOT NULL COMMENT 'Version for Optimistic Locking'")
    private Long version;

    protected ProductOption() {
    }

    public ProductOption(Product product, Option option, int quantity) {
        validateQuantity(quantity);
        this.product = product;
        this.option = option;
        this.quantity = quantity;
    }

    public void update(Option option, int quantity) {
        validateQuantity(quantity);
        this.option = option;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public void decreaseQuantity(int amount) {
        if (amount <= MIN_QUANTITY) {
            throw new BusinessException(ErrorCode.INVALID_DECREASE_QUANTITY);
        }
        int decreaseQuantity = this.quantity - amount;
        if (decreaseQuantity < MIN_QUANTITY) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_QUANTITY);
        }
        this.quantity = decreaseQuantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= MIN_QUANTITY || quantity >= MAX_QUANTITY) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }
}
