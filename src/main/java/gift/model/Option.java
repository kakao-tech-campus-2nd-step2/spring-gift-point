package gift.model;

import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.NAME_INVALID_CHARACTERS;
import static gift.util.constants.OptionConstants.NAME_SIZE_LIMIT;
import static gift.util.constants.OptionConstants.QUANTITY_MAX;
import static gift.util.constants.OptionConstants.QUANTITY_MIN;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Size(min = 1, max = 50, message = NAME_SIZE_LIMIT)
    @Pattern(
        regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]*$",
        message = NAME_INVALID_CHARACTERS
    )
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = QUANTITY_MIN)
    @Max(value = 99999999, message = QUANTITY_MAX)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
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

    public void update(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public boolean isProductIdMatching(Long productId) {
        return Objects.equals(this.product.getId(), productId);
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException(INSUFFICIENT_QUANTITY + this.id);
        }
        this.quantity -= quantity;
    }

    public boolean isNameMatching(String name) {
        return this.name.equals(name);
    }
}
