package gift.domain.model.dto;

import gift.domain.model.entity.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionUpdateRequestDto {

    @NotBlank
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_.,]+$",
        message = "허용되지 않는 특수문자가 포함되어 있습니다.")
    private final String optionName;

    @NotNull
    @Min(value = 1)
    @Max(value = 99999999)
    private final int optionQuantity;

    public OptionUpdateRequestDto(String optionName, int optionQuantity) {
        this.optionName = optionName;
        this.optionQuantity = optionQuantity;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public Option toEntity() {
        return new Option(this.optionName, this.optionQuantity);
    }
}