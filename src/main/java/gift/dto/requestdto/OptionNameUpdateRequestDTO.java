package gift.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionNameUpdateRequestDTO(
    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[\\w가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-&/_]*$", message = "( ), [ ], +, -, &, /, _ 외 특수문자는 사용할 수 없습니다.")
    String name) {
}
