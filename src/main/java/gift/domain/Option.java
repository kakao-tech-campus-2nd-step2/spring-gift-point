package gift.domain;

import gift.dto.request.AddOptionRequest;
import gift.dto.request.AddProductRequest;
import gift.exception.CustomException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import static gift.constant.ErrorMessage.*;
import static gift.exception.ErrorCode.INVALID_AMOUNT_ERROR;

@Entity
@Table(name = "option",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})})
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = REQUIRED_FIELD_MSG)
    @Size(max = 50, message = LENGTH_ERROR_MSG)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = SPECIAL_CHAR_ERROR_MSG)
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = REQUIRED_FIELD_MSG)
    @Min(value = 1, message = INVALID_QUANTITY_ERROR_MSG)
    @Max(value = 100_000_000, message = INVALID_QUANTITY_ERROR_MSG)
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(AddProductRequest request) {
        this.name = request.optionName();
        this.quantity = request.optionQuantity();
    }

    public Option(AddOptionRequest addOptionRequest, Product product) {
        this.name = addOptionRequest.name();
        this.quantity = addOptionRequest.quantity();
        this.product = product;
    }

    public void subtract(int amount) {
        checkAmount(amount);
        this.quantity -= amount;
    }

    private void checkAmount(int amount) {
        if (amount < 1 || amount > this.quantity) {
            throw new CustomException(INVALID_AMOUNT_ERROR);
        }
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

    public void setProduct(Product product) {
        this.product = product;
    }
}

