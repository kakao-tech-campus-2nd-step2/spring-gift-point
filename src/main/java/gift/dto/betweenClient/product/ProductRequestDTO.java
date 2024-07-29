package gift.dto.betweenClient.product;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record ProductRequestDTO(
        Long id,

        @NotBlank(message = "상품 이름을 입력해주세요.")
        @Length(min = 1, max = 15, message = "제품명 길이는 1~15자만 가능합니다.")
        @Pattern(regexp = "[a-zA-Z0-9ㄱ-ㅎ가-힣()\\[\\]+\\-&/_ ]+", message = "( ), [ ], +, -, &, /, _을 제외한 특수문자는 입력할 수 없습니다.")
        @Pattern(regexp = "^((?!카카오).)*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 후에 사용해주시기 바랍니다.")
        String name,

        @NotNull(message = "가격을 입력해주세요")
        @Min(value = 0, message = "가격은 0 이상, 2147483647 이하이여야 합니다.")
        @Max(value = Integer.MAX_VALUE, message = "가격은 0 이상, 2147483647 이하이여야 합니다.")
        Integer price,

        @NotBlank(message = "이미지 URL을 입력해주세요.")
        @URL(message = "URL 형식이 아닙니다.")
        String imageUrl,

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        @Length(min = 1, max = 15, message = "카테고리명 길이는 1~15자만 가능합니다.")
        String categoryName
) {
    public Product convertToProduct(Category category) {
        return new Product(this.name, this.price, this.imageUrl, category);
    }

}
