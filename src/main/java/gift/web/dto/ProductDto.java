package gift.web.dto;

import gift.domain.option.Option;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProductDto(
    Long id,
    @NotBlank
    @Size(max=15)
    @Pattern(regexp="^[가-힣 \\w\\(\\)\\[\\]\\+\\-\\&\\/]*$",
        message = "( ), [ ], +, -, &, /, _의 특수문자만 사용 가능합니다.")
    @Pattern(regexp="^(?!.*카카오).*$",
        message = "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    String name,
    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    Long price,
    String imageUrl,
    Long categoryId,
    List<OptionDto> optionDtoList
) {
}
