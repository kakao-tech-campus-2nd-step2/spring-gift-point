package gift.option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

public class OptionRequest {
    @NotBlank(message = "이름은 공백으로 둘 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9\\(\\)\\[\\]\\+\\-&/\\_ ]{1,51}$", message = "옵션명은 공백을 포함하여 최대 50자까지 입력할 수 있습니다. 특수문자는 ( ) [ ] + - & / _ 만 사용 가능합니다.")
    String optionName;
    @NotNull(message = "수량은 공백으로 둘 수 없습니다.") @Range(min = 1, max = 100000000, message = "최소 1개 이상 1억개 미만의 수량이 있어야 합니다.")
    long quantity;


    public OptionRequest() {
    }

    public OptionRequest(String name, Long quantity) {
        this.optionName = name;
        this.quantity = quantity;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public @NotNull(message = "수량은 공백으로 둘 수 없습니다.") @Range(min = 1, max = 100000000, message = "최소 1개 이상 1억개 미만의 수량이 있어야 합니다.") long getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "수량은 공백으로 둘 수 없습니다.") @Range(min = 1, max = 100000000, message = "최소 1개 이상 1억개 미만의 수량이 있어야 합니다.") long quantity) {
        this.quantity = quantity;
    }
}
