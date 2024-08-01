package gift.DTO.option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OptionRequest (
    @Length(max = 50, message = "The option name can include up to 50 characters, including spaces")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-&/_]*$",
        message = "Only the following special characters are allowed: (), [], +, -, &, /, _")
    String name,

    @Min(value = 1)
    @Max(value = 99999999)
    Long quantity
) {
}
