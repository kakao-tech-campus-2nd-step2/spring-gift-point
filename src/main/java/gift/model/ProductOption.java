package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "옵션 이름을 입력해주세요.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$", message = "특수 문자는 (),[],+,-,&,/,_만 가능합니다.")
    private String name;

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductOption() {
    }

    public ProductOption(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public ProductOption(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    // 수량 차감
    public void subtractQuantity(int quantityToSubtract) {
        if (this.quantity <= quantityToSubtract) {
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        this.quantity -= quantityToSubtract;
    }
}
