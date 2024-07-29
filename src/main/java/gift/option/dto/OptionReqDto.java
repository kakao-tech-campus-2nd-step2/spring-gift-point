package gift.option.dto;

import gift.option.entity.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OptionReqDto(
        @NotBlank(message = "옵션 이름은 필수 입력 값입니다.")
        @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력 가능합니다.")
        String name,

        @Min(value = 1, message = "옵션 수량은 최소 1개 이상 입력해야 합니다.")
        @Max(value = 99999999, message = "옵션 수량은 최대 99999999개까지 입력 가능합니다.")
        Integer quantity
) {

        public Option toEntity() {
                return new Option(name, quantity);
        }
}
