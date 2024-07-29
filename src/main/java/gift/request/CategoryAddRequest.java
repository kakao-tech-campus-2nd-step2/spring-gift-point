package gift.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CategoryAddRequest(
    @NotBlank(message = "카테고리를 입력해주세요.")
    @Length(min=1, max=20, message = "1~20자 사이로 입력해주세요.")
    String name
) {
}
