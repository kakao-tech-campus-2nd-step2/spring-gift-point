package gift.product.dto;

import gift.product.validator.KakaoValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        @Pattern(regexp = "[\\w\\d\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_\\uAC00-\\uD7A3]*", message = "특수 문자를 제외한 문자열을 입력해야 합니다.")
        @Size(max = 15, message = "길이가 15 이하여야 합니다.")
        @KakaoValidator
        String name,
        @NotNull
        @Min(value = 0, message = "0 이상의 정수를 입력해야 합니다.")
        int price,
        @NotNull
        String imageUrl,
        @NotNull
        String categoryName,
        @NotNull
        @Valid
        OptionRequest option) { }