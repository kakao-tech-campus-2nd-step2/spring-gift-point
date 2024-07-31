package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OptionDTO {

    @NotNull(message = "옵션명은 필수 항목입니다.")
    @Size(min = 1, max = 50, message = "옵션의 이름은 최소 1자부터 최대 50자 미만입니다.")
    private String name;

    @Min(value = 1, message = "수량은 최소 1개 이상, 최대 1억개 미만입니다.")
    @Max(value = 100_000_001, message = "수량은 최소 1개 이상, 최대 1억개 미만입니다.")
    private int quantity;

    public OptionDTO() {}

    public OptionDTO(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}