package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionDTO {
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z가-힣()\\[\\]\\+\\-&/_\s]+$")
    @Schema(description = "옵션명", nullable = false, example = "그레이 색상")
    private String name;
    @Min(1)
    @Max(99_999_999) // 100,000,000 미만
    @Schema(description = "옵션 수량", nullable = false, example = "5")
    private int quantity;

    public OptionDTO() {
    }

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
}
