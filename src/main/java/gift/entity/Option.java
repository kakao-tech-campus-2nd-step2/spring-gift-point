package gift.entity;

import gift.exception.InsufficientOptionQuantityException;
import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_option_product_id_ref_product_id"))
    private Product product;

    protected Option() {
    }

    public Option(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public Long getProductId() {
        return product.getId();
    }

    public void associateWithProduct(Product product) {
        this.product = product;
    }

    public void subtract(int subtractQuantity) {
        if (this.quantity < subtractQuantity) {
            throw new InsufficientOptionQuantityException(subtractQuantity);
        }
        this.quantity -= subtractQuantity;
    }

    public boolean isSameName(String newOptionName) {
        return name.equals(newOptionName);
    }
}
