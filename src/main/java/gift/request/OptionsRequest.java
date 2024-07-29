package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record OptionsRequest(
    @NotBlank(message = "옵션명을 입력해주세요.")
    @Length(min = 1, max = 50, message = "1~50자 사이로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9()+&/_ \\[\\]-]*$", message = "옵션명에 특수 문자는 '(, ), [, ], +, -, &, /, -' 만 입력 가능합니다.")
    @JsonProperty(value = "option_name")
    String optionName,
    @NotNull(message = "수량을 입력해주세요.")
    @Range(min = 1, max = 100000000, message = "옵션 수량은 1개~1억개 사이여야 합니다.")
    Integer quantity
) {

}
