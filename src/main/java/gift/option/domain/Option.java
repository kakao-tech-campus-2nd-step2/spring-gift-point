package gift.option.domain;

import gift.product.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]\\+\\-\\&\\/\\_]*$", message = "특수 문자 중 ()[]+-&/_만 사용 가능하며, 영어와 한국어만 허용됩니다.")
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "Quantity은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "Quantity는 최대 1억 미만 개까지 가능합니다.")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 활용 메서드들
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(Long quantity) {
        this.quantity -= quantity;
    }

    // 생성자
    public Option() {
    }

    public Option(String name, Long quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }
    public Option(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public Long getProductId() {
        return product.getId();
    }
}
