package gift.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class OptionRequest {

    @NotEmpty(message = "옵션 이름은 비어있을 수 없습니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s()\\[\\]+\\-&/_]+$",
        message = "옵션 이름에는 영문자, 숫자, 공백 및 허용된 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
    private String optionName;

    @Range(min = 1, max = 99999999, message = "옵션 수량은 1 이상 99,999,999 이하여야 합니다.")
    private int optionQuantity;

    public OptionRequest(String optionName, int optionQuantity) {
        this.optionName = optionName;
        this.optionQuantity = optionQuantity;
    }


    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public void setOptionQuantity(int optionQuantity) {
        this.optionQuantity = optionQuantity;
    }
}
