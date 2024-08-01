package gift.entity;

import gift.constants.ErrorMessage;
import gift.dto.OptionEditRequest;
import gift.dto.OptionSubtractRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
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

    public void updateOption(OptionEditRequest editRequest) {
        name = editRequest.getName();
        quantity = editRequest.getQuantity();
    }

    public int subtractQuantity(OptionSubtractRequest subtractRequest) {
        int quantity = subtractRequest.getQuantity();
        if (!canSubtractQuantity(quantity)) {
            throw new IllegalArgumentException(ErrorMessage.OPTION_QUANTITY_FEWER_MSG);
        }
        return this.quantity -= quantity;
    }

    private boolean canSubtractQuantity(int quantity) {
        return this.quantity >= quantity;
    }
}
