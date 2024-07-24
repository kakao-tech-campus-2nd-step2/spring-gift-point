package gift.category.model.dto;

import static gift.util.Utils.NAME_PATTERN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public record CategoryRequest(
        @NotBlank
        @Size(max = 15, message = "카테고리 이름은 15자 이하로 입력해주세요.")
        @Pattern(regexp = NAME_PATTERN, message = "사용할 수 없는 특수문자가 포함되어 있습니다.")
        String name,

        @Nullable
        String description

) {
}
