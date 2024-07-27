package gift.option.domain;

import jakarta.validation.constraints.*;

public class OptionRequest {

    @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]\\+\\-\\&\\/\\_]*$", message = "특수 문자 중 ()[]+-&/_만 사용 가능합니다.")
    private String name;

    @Min(value = 1, message = "Quantity은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "Quantity는 최대 1억 미만 개까지 가능합니다.")
    private Long quantity;

    @NotNull(message = "Product ID는 필수입니다.")
    private Long productId;

    // Getters and setters
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
