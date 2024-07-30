package gift.domain.product.dto.request;

import gift.domain.option.dto.OptionRequestDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.URL;

public record ProductRequest(

    @NotBlank(message = "이름이 입력되지 않았습니다")
    @Size(max = 15, message = "이름의 길이는 15자 이내여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = "특수문자는 ( ), [ ], +, -, &, /, _ 만 사용 가능합니다.")
    String name,
    @NotNull
    Long categoryId,
    @NotNull(message = "상품 가격이 입력되지 않았습니다.")
    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    Integer price,

    @NotBlank(message = "상품 설명이 입력되지 않았습니다.")
    String description,

    @NotBlank(message = "이미지 url 이 입력되지 않았습니다.")
    @URL(message = "이미지 url 형식이 올바르지 않습니다.")
    String imageUrl,

    @NotEmpty(message = "옵션은 최소 한 개 이상이어야 합니다.")
    List<OptionRequestDTO> options
) {

}
