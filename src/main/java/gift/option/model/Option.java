package gift.option.model;

import gift.common.exception.OptionException;
import gift.option.OptionErrorCode;
import gift.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
        product.addOption(this);
    }

    public Option(Long id, String name, Integer quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
        product.addOption(this);
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

    public void updateInfo(String name, Integer quantity) throws OptionException {
        this.name = name;
        this.quantity = quantity;
        product.validateOptions();
    }

    public void subtract(Integer quantity) throws OptionException {
        if (this.quantity < quantity) {
            throw new OptionException(OptionErrorCode.NOT_ENOUGH_QUANTITY);
        }
        this.quantity = this.quantity - quantity;
    }

    public static class Validator {

        public static void validateOptionCount(List<Option> options) throws OptionException {
            if (options.size() <= 1) {
                throw new OptionException(OptionErrorCode.OPTION_COUNT_ONE);
            }
        }
    }
}
