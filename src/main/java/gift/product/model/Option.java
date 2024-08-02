package gift.product.model;

import static gift.product.exception.GlobalExceptionHandler.CANNOT_SUBTRACT_ZERO_OR_NEGATIVE;
import static gift.product.exception.GlobalExceptionHandler.SUBTRACT_EXCEED_QUANTITY;

import gift.product.exception.InstanceValueException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9 ()\\[\\]+\\-\\&/\\_]*$", message = "( ), [ ], +, -, &, /, _ 외의 특수문자는 사용이 불가합니다.")
    private String name;

    @Column(nullable = false)
    @PositiveOrZero(message = "옵션의 갯수는 음수로 설정할 수 없습니다.")
    private int quantity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    public Option() {

    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
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

    public boolean isSameName(Option option) {
        if(Objects.equals(option.id, this.id))
            return false;
        return this.name.equals(option.getName());
    }

    public void subtractQuantity(int amountToSubtract) {
        this.quantity -= amountToSubtract;
    }
}
