package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Option() {
    }

    public Option(Long id, String name, Long quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
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

    public Long getQuantity() {
        return quantity;
    }

    public void updateOption(String name, Long quantity) {
        validateName(name);
        validateQuantity(quantity);
        this.name = name;
        this.quantity = quantity;
    }

    public void subtractQuantity(Long subtractQuantity) {
        if (this.quantity < subtractQuantity) {
            throw new IllegalArgumentException("차감할 수량이 현재 수량보다 많을 수 없습니다.");
        }
        this.quantity = this.quantity - subtractQuantity;
    }


    public Product getProduct() {
        return product;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("옵션 이름은 최소 1자 이상이어야 합니다.");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("옵션 이름은 최소 1자 이상이어야 합니다.");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("옵션 이름은 공백 포함 최대 50자까지 입력할 수 있습니다.");
        }
        if (!name.matches(
            "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$")) {
            throw new IllegalArgumentException("옵션 이름에 (), [], +, -, &, /, _ 외 특수 문자는 사용할 수 없습니다.");
        }
    }

    private void validateQuantity(Long quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상이어야 합니다.");
        }
        if (quantity == 0) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상이어야 합니다.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상이어야 합니다.");
        }
        if (quantity > 100_000_000) {
            throw new IllegalArgumentException("옵션 수량은 최대 1억 개 미만까지 가능합니다.");
        }
    }
}
