package gift.model.option;

import gift.common.exception.AlreadyExistName;
import gift.model.product.Product;
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
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) // 동일한 옵션 생성 방지
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(max = 15, message = "이름의 최대 글자수는 15입니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$",
        message = "상품 이름은 최대 15자, 한글과 영문, 그리고 특수기호([],(),+,-,&,/,_)만 사용 가능합니다!"
    )
    private String name;

    @Min(value = 1, message = "옵션의 수량은 최소 1개 이상입니다.")
    @Max(value = 9999999, message = "옵션의 최대 수량은 1억개 미만입니다.")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void update(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    public Option() {}
    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    // 옵션 수량 삭제 메서드
    public void subtract(int amount){
        if(this.quantity < amount) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
        this.quantity -= amount;
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
}
