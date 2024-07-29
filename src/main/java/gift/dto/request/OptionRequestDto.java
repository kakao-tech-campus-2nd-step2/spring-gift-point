package gift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OptionRequestDto(

        @Pattern(
                regexp = "^[0-9가-힣a-zA-Z()\\[\\]+\\-&/_\\s]{1,50}$",
                message = "옵션 이름이 잘못 되었습니다."
        )
        String optionName,

        @NotNull(message = "수량을 입력하세요 (1 이상 1억 미만)")
        @Min(1)
        @Max(99999999)
        Integer optionQuantity
) { }
