package gift.option.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode
@ToString
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(max = 50, message = "이름은 최대 50자입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$", message = "옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.")
    private String name;

    @Setter
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity) {
        this(name, quantity, null);
    }


    public Option(String name, int quantity, Product product) {
        if (name.length() > 50) {
            throw new IllegalArgumentException("이름은 최대 50자입니다.");
        }
        if (!name.matches("^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_\\s]*$")) {
            throw new IllegalArgumentException("옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.");
        }
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new IllegalArgumentException("수량은 1개 이상 1억개 미만이어야 합니다.");
        }
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void subtract(int quantity) {
        this.setQuantity(this.quantity - quantity);
    }

    public @Size(max = 50, message = "이름은 최대 50자입니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$", message = "옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.") String getName() {
        return name;
    }

    public Option setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Option setId(Long id) {
        this.id = id;
        return this;
    }

    public Option setName(String name) {
        this.name = name;
        return this;
    }

}
