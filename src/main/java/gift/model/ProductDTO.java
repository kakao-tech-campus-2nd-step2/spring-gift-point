package gift.model;

import gift.validation.KakaoValid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProductDTO(

    @NotNull(message = "ID는 필수 입력사항 입니다.") Long id,
    @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]\\-&/_+\\s]{1,15}$", message = "최대 공백 포함 15글자만 가능합니다. \n또한 특수 문자는 (, ), [, ], +, -, _, &, / 만 가능합니다.")
    @KakaoValid String name,
    @NotNull(message = "가격은 필수 입력사항 입니다.") Long price,
    @NotEmpty(message = "imageURL은 필수 입력사항 입니다.") String imageUrl,
    Long categoryId
) {

}