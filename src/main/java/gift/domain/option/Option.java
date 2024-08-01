package gift.domain.option;

import gift.domain.product.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity) {
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

    public void addProduct(Product product) {
        this.product = product;
    }

    public boolean hasSameName(Option target) {
        return this.name.equals(target.getName());
    }

    public void subtract(int target) {
        if (target >= this.quantity) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY, this.quantity);
        }
        this.quantity = this.quantity - target;
    }
}
