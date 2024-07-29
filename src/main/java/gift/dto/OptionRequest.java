package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequest {

    @NotBlank(message = "옵션 이름은 필수 항목 입니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 가능 합니다.")
    @Pattern(regexp = "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_가-힣]*$", message = "옵션 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.")
    private String name;

    @Min(value = 1, message = "0개 이하는 등록할 수 없습니다.")
    @Max(value = 99999999, message = "판매 가능한 수량은 1억 개 미만 입니다.")
    private int quantity;

    public OptionRequest() {
    }

    public OptionRequest(String name, int quantity) {
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
