package gift.product.model.dto.option;


import gift.BaseTimeEntity;
import gift.product.exception.ProductCustomException.NotEnoughStockException;
import gift.product.model.dto.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "option", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "name"})})
@SQLDelete(sql = "UPDATE option SET deletion_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deletion_date IS NULL")
public class Option extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 50)
    private String name;

    @Min(1)
    @Max(100000000 - 1)
    @Column(nullable = false)
    private int quantity;

    @Min(0)
    @Column(nullable = false)
    private int additionalCost = 0;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option(String name, Integer quantity, Integer additionalCost, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
        this.product = product;
    }

    public Option() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAdditionalCost() {
        return additionalCost;
    }

    public Product getProduct() {
        return product;
    }

    public void updateOption(String name, int quantity, int additionalCost) {
        this.name = name;
        this.quantity = quantity;
        this.additionalCost = additionalCost;
    }

    public boolean isOwner(Long productId) {
        return product.getId().equals(productId);
    }

    public void subtract(int quantity) {
        if (this.quantity < quantity) {
            throw new NotEnoughStockException();
        }
        this.quantity -= quantity;
    }

}
