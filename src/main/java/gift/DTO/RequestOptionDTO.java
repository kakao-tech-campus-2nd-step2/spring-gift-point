package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "옵션 요청 DTO")
public record RequestOptionDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-\\&/_]*$",
                message = "옵션 이름에는 허용된 특수 문자만 포함될 수 있습니다: (), [], +, -, &, /, _"
        )
        @Size(max = 50,
                message = "옵션 이름의 길이는 공백을 포함하여 최대 50자 입니다")
        @NotBlank (message =  "옵션 이름은 필수입니다")
        @Schema(description = "옵션 이름")
        String name,

        @Min(value = 1, message = "상품 옵션의 수량은 최소 1개이상이여야 합니다")
        @Max(value = 99999999, message = "상품 옵션의 수량은 최대 1억개미만이여야 합니다")
        @Schema(description = "옵션 수량")
        int quantity
){
}
