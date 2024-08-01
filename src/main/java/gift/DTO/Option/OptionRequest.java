package gift.DTO.Option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequest {
    @Size(max=50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$",
            message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 허용되며, 한글, 영어, 숫자만 입력 가능합니다.")
    String name;
    @Min(1)
    @Max(99999999)
    int quantity;

    public OptionRequest(){

    }

    public OptionRequest(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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
