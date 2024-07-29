package gift.DTO;

import gift.Model.Entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "상품 요청 DTO")
public record RequestProductDTO(
        @Pattern(
                regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-\\&/_]*$",
                message = "상품 이름에는 허용된 특수 문자만 포함될 수 있습니다: (), [], +, -, &, /, _"
        )
        @Pattern(regexp = "^(?!.*카카오).*$",
                message = "상품 이름에 '카카오'가 포함된 문구는 담당 MD와 협의가 필요합니다.")
        @NotBlank(message = "상품 이름은 필수입니다")
        @Size(max = 15, message = "상품 이름은 최대 15자 입니다.")
        @Schema(description = "상품 이름")
        String name,

        @Schema(description = "상품 가격")
        int price,

        @Schema(description = "상품 imageUrl")
        String imageUrl,

        @NotNull(message = "카테고리 Id값은 필수입니다")
        @Min(value = 1, message = "카테고리Id값은 최소 1이상입니다")
        @Schema(description = "카테고리 Id")
        Long categoryId
) {
        public static RequestProductDTO of(Product product){
                return new RequestProductDTO(product.getName().getValue(), product.getPrice().getValue(), product.getImageUrl().getValue(), product.getId());
        }
}

