package gift.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;

@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "옵션 ID(자동으로 설정)", defaultValue = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    @Schema(description = "옵션에 해당하는 상품")
    private Product product;

    @Column(name = "name", unique = true)
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Size(max = 50, message = "길이가 50를 넘을 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+&/_ ]*$", message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용이 불가합니다.")
    @Schema(description = "옵션 이름", defaultValue = "옵션 이름")
    private String name;

    @Column(name = "quantity")
    @Positive(message = "옵션의 개수는 0이하 일 수 없습니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 1억개 이상 일 수 없습니다.")
    @Schema(description = "상품 개수(1~100,000,000)", defaultValue = "10")
    private int quantity;

    protected Option(){

    }

    @ConstructorProperties({"id","product","name","quantity"})
    public Option(Long id, Product product, String name, int quantity) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
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

    public int subtract(int count) {
        if (count <= 0 ){
            throw new IllegalArgumentException("0이하는 뺼수 없습니다.");
        }
        if(quantity < count){
            throw new IllegalArgumentException("빼려는 수보다 옵션의 수량이 작습니다.");
        }
        if(quantity == count ){
            return 0; // option의 quantity가 0이되면 validation에 걸리므로 0을 리턴 후 service에서 삭제
        }
        return quantity -= count;
    }
}
