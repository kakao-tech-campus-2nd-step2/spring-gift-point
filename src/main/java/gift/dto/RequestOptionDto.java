package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RequestOptionDto {

    @Size(min = 0, max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다. ")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()*\\[\\]+\\-&/_]*$", message = "( ), [ ], +, -, &, /, _ 외 특수 문자 사용 불가능합니다. ")
    private final String name;

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    private final int quantity;

    public RequestOptionDto(String name, int quantity) {
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
