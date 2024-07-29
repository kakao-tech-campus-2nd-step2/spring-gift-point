package gift.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_.,]+$",
        message = "허용되지 않는 특수문자가 포함되어 있습니다.")
    @Column(nullable = false)
    private String name;

    @Min(1)
    @Max(99999999)
    @Column(nullable = false)
    private int quantity;

    public Option() {
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(Product product, String name, int quantity) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void update(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("감소시킬 수량은 양수여야 합니다.");
        }
        if (this.quantity < quantity) {
            throw new IllegalStateException("현재 재고량보다 많은 수량을 감소시킬 수 없습니다.");
        }
        this.quantity -= quantity;
    }
}