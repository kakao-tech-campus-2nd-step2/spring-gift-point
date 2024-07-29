package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "상품 등록 요청 DTO")
public record RequestProductPostDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-\\&/_]*$",
                message = "상품 이름에는 허용된 특수 문자만 포함될 수 있습니다: (), [], +, -, &, /, _"
        )
        @Pattern(regexp = "^(?!.*카카오).*$",
                message = "상품 이름에 '카카오'가 포함된 문구는 담당 MD와 협의가 필요합니다.")
        @NotNull(message = "상품 이름은 필수입니다")
        @Size(max = 15, message = "상품 이름은 최대 15자 입니다.")
        @Schema(description = "상품 이름")
        String name,

        @Min(value = 1, message = "상품 가격은 최소한 1원 이상입니다")
        @Schema(description = "상품 가격")
        int price,

        @NotBlank(message = "imageUrl값은 필수입니다")
        @Schema(description = "상품 imageUrl")
        String imageUrl,

        @NotNull(message = "카테고리 Id값은 필수입니다")
        @Min( value = 1, message = "categoryId값은 최소 1이상입니다")
        @Schema(description = "카테고리 Id")
        Long categoryId,

        @Pattern(
                regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-\\&/_]*$",
                message = "옵션 이름에는 허용된 특수 문자만 포함될 수 있습니다: (), [], +, -, &, /, _"
        )
        @Size(max = 50,
                message = "옵션 이름의 길이는 공백을 포함하여 최대 50자 입니다")
        @NotBlank(message = "옵션 이름은 필수입니다")
        @Schema(description = "옵션 이름")
        String optionName,

        @NotNull(message = "옵션 수량은 필수입니다.")
        @Min(value = 1, message = "상품 옵션의 수량은 최소 1개이상이여야 합니다")
        @Max(value = 99999999, message = "상품 옵션의 수량은 최대 1억개미만이여야 합니다")
        @Schema(description = "옵션 수량")
        Integer optionQuantity
){}
