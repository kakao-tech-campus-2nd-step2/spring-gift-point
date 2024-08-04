package gift.option.domain;

import gift.exception.type.InvalidOptionQuantityException;
import gift.exception.type.InvalidProductOptionException;
import gift.product.domain.Product;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity) {
        this(null, name, quantity);
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;

        validateName();
        validateQuantity();
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

    public void subtractQuantity(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new InvalidOptionQuantityException("수량이 0 이하가 될 수 없습니다.");
        }
        this.quantity -= quantity;
    }

    public void validateName() {
        if (this.name == null || this.name.isBlank() || this.name.length() > 50) {
            throw new InvalidProductOptionException("옵션 이름은 공백을 포함하여 최대 50자까지 입력 할 수 있습니다.");
        }
        if (!this.name.matches("^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&\\/\\_\\s]*$")) {
            throw new InvalidProductOptionException("옵션 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.");
        }
    }

    public void validateQuantity() {
        if (this.quantity < 1 || this.quantity >= 100_000_000) {
            throw new InvalidProductOptionException("옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return id.equals(option.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void update(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;

        validateName();
        validateQuantity();
    }
}
