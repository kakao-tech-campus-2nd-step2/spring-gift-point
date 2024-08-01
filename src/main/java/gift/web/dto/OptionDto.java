package gift.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionDto(
    Long id,

    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[가-힣ㄱ-ㅎ  \\w\\(\\)\\[\\]\\+\\-\\&\\/]*$]",
        message = "( ), [ ], +, -, &, /, _의 특수문자만 사용 가능합니다.")
    String name,

    @Min(1)
    @Max(9999_9999)
    Long quantity
)
{ }
