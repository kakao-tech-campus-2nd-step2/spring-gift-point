package gift.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "옵션 이름을 입력해주세요.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]\\+\\-\\&/_]*$", message = "옵션 이름에 허용되지 않은 특수문자가 포함되어 있습니다.")
    private String name;

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "수량은 1억 개 미만이어야 합니다.")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {

    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public @NotBlank(message = "옵션 이름을 입력해주세요.") @Size(max = 50, message = "옵션 이름은 최대 50자까지 가능합니다.") @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]\\+\\-\\&/_]*$", message = "옵션 이름에 허용되지 않은 특수문자가 포함되어 있습니다.") String getName() {
        return name;
    }

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "수량은 1억 개 미만이어야 합니다.")
    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int quantityToSubtract) {
        if (this.quantity < quantityToSubtract) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        this.quantity -= quantityToSubtract;
    }
}
