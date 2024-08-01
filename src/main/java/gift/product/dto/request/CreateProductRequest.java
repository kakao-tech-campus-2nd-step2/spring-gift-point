package gift.product.dto.request;

import gift.validation.annotation.RestrictedKeyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateProductRequest(
    @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9 ()\\[\\]+\\-&/_]*$",
        message = "상품 이름은 특수문자는 ( ), [ ], +, -, &, /, _만 사용 가능하며, 한글, 영어, 숫자만 입력할 수 있습니다.")
    @RestrictedKeyword(keywords = {"카카오"}, message = "\"카카오\"가 포함된 문구는 담당 MD와 협의가 필요합니다.")
    String name,

    @Positive Integer price,

    @NotNull
    @NotBlank
    String imageUrl,

    @NotNull
    Long categoryId,

    @NotNull
    @NotEmpty
    List<NewOption> options
) {

}
