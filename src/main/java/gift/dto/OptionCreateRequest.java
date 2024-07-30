package gift.dto;

import gift.annotation.OptionName;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class OptionCreateRequest {

    @OptionName(message = "이름 규칙을 준수하여야 합니다.")
    private String name;

    @DecimalMax(value = "100000000", message = "상품의 개수는 1억개 이하여야 합니다.")
    @DecimalMin(value = "1", message = "상품의 개수는 1개 이상이어야 합니다.")
    private int quantity;

    public OptionCreateRequest(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
