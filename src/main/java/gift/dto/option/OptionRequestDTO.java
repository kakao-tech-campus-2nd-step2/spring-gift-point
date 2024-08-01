package gift.dto.option;

import jakarta.validation.constraints.*;

public record OptionRequestDTO(
        @NotNull
        @Size(max=50, message = "이름이 50글자를 초과하였습니다.")
        @Pattern(
                regexp = "^[a-zA-Z0-9-ㄱ-하-ㅣ()\\[\\]+\\-\\&/_\\s]*$",
                message = "이름에 허용되지 않은 특수 문자가 포함되었습니다."
        )
        String name,

        @NotNull
        @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
        @Max(value = 100000000, message = "수량은 최대 1억 이하여야 합니다.")
        Long quantity) {
}
