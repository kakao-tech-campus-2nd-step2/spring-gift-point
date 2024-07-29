package gift.product;

import static gift.exception.ErrorMessage.PRODUCT_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.PRODUCT_NAME_KAKAO_STRING;
import static gift.exception.ErrorMessage.PRODUCT_NAME_LENGTH;

import gift.category.CategoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "상품 수신용 DTO")
public record ProductDTO(
    @Length(min = 1, max = 15, message = PRODUCT_NAME_LENGTH)
    // 제품의 이름은 반드시 영어, 한글, 숫자, 그리고 특수문자 (, ), [, ], +, -, &, /, _ 만 혀용한다.
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_]*$", message = PRODUCT_NAME_ALLOWED_CHARACTER)
    // 제품의 이름에 대소문자 구문없이 kakao, 카카오라는 이름이 들어가서는 안된다.
    @Pattern(regexp = "^(?i)(?!.*(kakao|카카오)).*$", message = PRODUCT_NAME_KAKAO_STRING)
    @Schema(description = "상품 명")
    String name,

    @Schema(description = "상품 가격")
    int price,

    @Schema(description = "상품 이미지 url")
    String imageUrl,

    @Schema(description = "상품 카테고리 정보")
    CategoryDTO category
) {

}
