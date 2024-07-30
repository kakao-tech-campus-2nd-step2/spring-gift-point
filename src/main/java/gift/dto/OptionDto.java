package gift.dto;

import jakarta.validation.constraints.*;

public class OptionDto {

    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_()+&/ ]*$", message = "이름에는 특수 문자는 (, ), [, ], +, -, &, /, _ 만 사용 가능합니다.")
    @NotBlank(message = "Option 이름은 필수값입니다.")
    private String name;
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    private int quantity;

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
