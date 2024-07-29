package gift.model;

import gift.exception.InputException;
import gift.exception.option.OptionsQuantityException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import org.springframework.util.StringUtils;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_options",
        columnNames = {"name"}
    )
})
public class Options extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_options_product_id_ref_product_id"))
    private Product product;
    @Version
    private Long version;

    protected Options() {
    }

    public Options(Long id, String name, Integer quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
        validateProduct(product);
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Options(String name, Integer quantity, Product product) {
        validateName(name);
        validateQuantity(quantity);
        validateProduct(product);
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

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void updateOption(String newName, Integer newQuantity) {
        validateName(newName);
        validateQuantity(newQuantity);
        this.name = newName;
        this.quantity = newQuantity;
    }

    public void subtractQuantity(Integer subQuantity) {
        validateQuantity(subQuantity);
        if (quantity < subQuantity) {
            throw new OptionsQuantityException();
        }

        this.quantity -= subQuantity;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name) || name.length() > 50) {
            throw new InputException("1~50자 사이로 입력해주세요.");
        }
        if (!name.matches("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9()+&/_ \\[\\]-]*$")) {
            throw new InputException("상품명에 특수 문자는 '(, ), [, ], +, -, &, /, -' 만 입력 가능합니다.");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0 || quantity > 100000000) {
            throw new InputException("수량은 1개~1억개 사이여야 합니다.");
        }
    }

    private void validateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new InputException("알 수 없는 오류가 발생하였습니다.");
        }
    }

}
