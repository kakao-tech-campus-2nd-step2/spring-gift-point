package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "options", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "product_id"}))
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '옵션 ID'")
    private Long id;

    @NotNull(message = "이름을 입력해주세요.")
    @Length(min = 1, max = 50, message = "1자 ~ 50자까지 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]*$", message = "사용불가한 특수 문자가 포함되어 있습니다.")
    @Column(nullable = false)
    private String name;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {}

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Option management methods
    public void assignProduct(Product product) {
        if (this.product != null) {
            this.product.getOptions().remove(this);
        }
        if (product != null) {
            this.product = product;
            if (!product.getOptions().contains(this)) {
                product.getOptions().add(this);
            }
        }
    }

    public void removeProduct() {
        this.product = new Product();
    }

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Insufficient quantity to decrease");
        }
        this.quantity -= amount;
    }

    // Getters
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


}