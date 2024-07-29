package gift.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class OptionRequest {
    @NotEmpty(message = "Product ID can not be empty")
    private Long productId;
    @NotEmpty(message = "옵션 이름은 비어있을 수 없습니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s()\\[\\]+\\-&/_]+$",
        message = "옵션 이름에는 영문자, 숫자, 공백 및 허용된 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
    private String name;

    @Range(min = 1, max = 99999999, message = "옵션 수량은 1 이상 99,999,999 이하여야 합니다.")
    private int quantity;

    public OptionRequest(Long productId, String name, int quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
