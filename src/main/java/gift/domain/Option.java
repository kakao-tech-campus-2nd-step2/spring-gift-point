package gift.domain;

import gift.constants.Messages;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.util.List;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public Option() {
    }

    public Option(String name, int quantity) {
        validateOptionName(name);
        validateOptionQuantity(quantity);
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.setOption(this);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void removeProduct() {
        if (this.product != null) {
            this.product.getOptions().remove(this);
            this.product = null;
        }
    }

    public void subtract(int subtrahend){
        int newQuantity = quantity - subtrahend;
        validateOptionQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    // 도메인 객체 검증 로직

    // 옵션 상품 이름 검증
    private void validateOptionName(String optionName){
        if (optionName == null || optionName.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름을 비우거나 공백으로 설정할 수 없습니다");
        }
        if (optionName.length() > 50) {
            throw new IllegalArgumentException("옵션명은 공백 포함하여 최대 50자까지 입력할 수 있습니다");
        }
        if (!optionName.matches("^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_가-힣]*$")) {
            throw new IllegalArgumentException("특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.");
        }
    }

    // 옵션 quantity 검증
    private void validateOptionQuantity(int quantity){
        if(quantity < 0 || quantity > 100000000){
            throw new IllegalArgumentException(Messages.QUANTITY_OUT_OF_RANGE_MESSAGE);
        }
    }

}
