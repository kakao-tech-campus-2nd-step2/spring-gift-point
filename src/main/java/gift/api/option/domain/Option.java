package gift.api.option.domain;

import gift.api.option.exception.InvalidSubtractionException;
import gift.api.product.domain.Product;
import gift.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_option_product_id_ref_product_id"), nullable = false)
    private Product product;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;

    protected Option() {
    }

    public Option(Product product, String name, Integer quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(Integer quantity) {
        if (this.quantity < quantity) {
            throw new InvalidSubtractionException();
        }
        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        return "Option{" +
            "name='" + name + '\'' +
            ", quantity=" + quantity +
            '}';
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
