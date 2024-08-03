package gift.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product_option", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "name"}))
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_option_id;

    @Column(nullable = false, length = 50)
    @Size(min = 1, max = 50, message = "Option 이름은 공백 포함 50문자 이내여야 합니다.")
    //@Pattern(regexp = "^[a-zA-Z0-9\\s()\\[\\]+\\-&/\\_]*$", message = "허용되지 않는 특수 문자는 (, ), [, ], +, -, &, /, _ 입니다.")
    private String name;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 1000)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public Option() {

    }

    public void subtract(Integer amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("감소할 수량은 0보다 커야 합니다.");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }
        this.quantity -= amount;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return product_option_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setId(long l) {
        this.product_option_id = l;
    }

    public Product getProduct() {
        return product;
    }
}
