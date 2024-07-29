package gift.model;

import gift.common.exception.ErrorCode;
import gift.common.exception.OptionException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "options")
public class Option extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public Product getProduct() {
        return product;
    }

    public boolean isDuplicateName(String name) {
        return this.name.equals(name);
    }

    public void updateOption(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.product.updateOption(this);
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int count) {
        if (quantity < count) {
            throw new OptionException(ErrorCode.NOT_ENOUGH_QUANTITY);
        }
        quantity -= count;
    }
}
