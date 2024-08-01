package gift.dto.product;

import static gift.util.Utils.NAME_PATTERN;

import gift.dto.option.CreateOptionRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.lang.Nullable;

public record CreateProductAdminRequest(
        @NotBlank
        @Size(max = 15, message = "상품 이름은 15자 이하로 입력해주세요.")
        @Pattern(regexp = NAME_PATTERN, message = "사용할 수 없는 특수문자가 포함되어 있습니다.")
        String name,

        @NotNull
        @Min(value = 0, message = "상품 가격은 0 이상의 정수로 입력해주세요.")
        int price,

        @Nullable
        String imageUrl,

        @NotNull
        Long sellerId,

        @NotNull
        Long categoryId,

        @NotNull
        List<CreateOptionRequest> options
) {
}
