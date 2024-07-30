package gift.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OptionRequest {
    private final static String SPECIAL_REGEX = "^[()\\[\\]+\\-&/_ㄱ-하-ㅣ가-힣a-zA-Z0-9\\s.,]*$";

    public record InitOption(
            @NotBlank
            @Pattern(regexp = SPECIAL_REGEX,
                    message = "( ), [ ], +, -, &, /, _ 을 제외한 특수 문자는 사용이 불가합니다.")
            String name,
            @Min(1) @Max(100_000_000)
            int quantity
    ) {
    }

    public record CreateOption(
            @NotBlank
            @Pattern(regexp = SPECIAL_REGEX,
                    message = "( ), [ ], +, -, &, /, _ 을 제외한 특수 문자는 사용이 불가합니다.")
            String name,
            @Min(1) @Max(100_000_000)
            int quantity,
            @Min(1)
            Long productId
    ) {
    }

    public record UpdateOption(
            @Min(1)
            Long id,
            @NotBlank
            @Pattern(regexp = SPECIAL_REGEX,
                    message = "( ), [ ], +, -, &, /, _ 을 제외한 특수 문자는 사용이 불가합니다.")
            String name,
            @Min(1) @Max(100_000_000)
            int quantity,
            @Min(1)
            Long productId
    ) {
    }
}
