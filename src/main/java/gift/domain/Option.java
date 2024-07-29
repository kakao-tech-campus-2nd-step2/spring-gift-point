package gift.domain;

import gift.dto.OptionDto;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "name"})})
@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false, length = 50)
    private String name;

    @Min(0)
    @Max(99999999)
    @Column(nullable = false)
    private Long quantity;

    @Version
    private Long version;

    public Option() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Option(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public OptionDto toDto() {
        return new OptionDto(this.id, this.name, this.quantity);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void subtract(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new GiftException(ErrorCode.INVALID_QUANTITY);
        }
        if (this.quantity < quantity) {
            throw new GiftException(ErrorCode.QUANTITY_CANNOT_BE_LESS_THAN_ZERO);
        }
        this.quantity -= quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
