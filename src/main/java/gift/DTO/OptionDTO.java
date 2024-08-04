package gift.DTO;

import gift.Model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;

public class OptionDTO {
    private Long id;
    @NotNull(message = "입력은 공백일 수 없습니다.")
    @Schema(description = "옵션에 해당하는 상품")
    private Product product;
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Size(max = 50, message = "길이가 50를 넘을 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+&/_ ]*$", message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용이 불가합니다.")
    @Schema(description = "옵션 이름", defaultValue = "옵션 이름")
    private String name;
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Positive(message = "옵션의 개수는 0이하 일 수 없습니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 1억개 이상 일 수 없습니다.")
    @Schema(description = "상품 개수(1~100,000,000)", defaultValue = "10")
    private int quantity;

    @ConstructorProperties({"id","product","name","quantity"})
    public OptionDTO(Long id, Product product, String name, int quantity) {
        this.id = id;
        this.product = product;
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
}
