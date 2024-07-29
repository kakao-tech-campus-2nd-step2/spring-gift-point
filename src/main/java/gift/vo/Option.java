package gift.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_option_product_id_ref_product_id"))
    private Product product;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private int quantity;

    public Option(Long id, Product product, String name, int quantity) {
        validateName(name);
        validateQuantity(quantity);
        this.id = id;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Option() {

    }

    private static void validateName(String name) {
        if (name == null || name.length() > 50) {
            throw new IllegalArgumentException("옵션명은 공백 포함하여 최대 50자까지 가능합니다.");
        }
        if (!name.matches("^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/]*$")) {
            throw new IllegalArgumentException("상품명에 () [] + - & / 외의 특수기호는 불가합니다");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 100000000) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상 1억 개 미만이여야 합니다.");
        }
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("해당 상품 옵션의 재고가 선택하신 수량 보다 작습니다. " + "[남은 수량: "+this.quantity+"]" );
        }
        if (this.quantity == 1 && quantity == 1) {
            throw new IllegalArgumentException("해당 상품 옵션의 재고가 하나 남았습니다.\n 수량은 최소 1개 이상이어야 합니다.");
        }
        this.quantity -= quantity;
    }
}
