package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequestDto {

    @NotEmpty(message = "옵션 이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-&/_ ]*$", message = "( ), [ ], +, -, &, /, _ 의 특수문자만 사용가능합니다.")
    @Size(max = 50, message = "옵션 이름은 공백포함 최대 50자 입니다.")
    private final String name;

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 999999999, message = "옵션 수량은 1억 미만이어야 합니다.")
    private final Long quantity;

    public OptionRequestDto(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}