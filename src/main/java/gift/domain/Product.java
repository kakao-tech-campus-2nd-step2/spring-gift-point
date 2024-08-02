package gift.domain;

import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.validator.constraints.Length;

public class Product {

    public record ProductRequest(
        @NotBlank(message = "상품 이름을 입력해 주세요.")
        @Length(max = 15, message = "상품 이름은 15자 이내로 입력해 주세요")
        @Pattern.List({
            @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]*$", message = "( ), [ ], +, -, &, /, _ 을 제외한 특수 문자는 사용이 불가합니다."),
            @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오' 포함된 경우 담당 MD와 협의가 필요합니다.")
        })
        String name,
        @Min(-1)
        Long price,
        @NotBlank(message = "이미지URL을 입력해 주세요.")
        @Pattern(regexp = "^(http(s?):)([/|.\\w|\\s|-])*\\.(?:jpg|gif|png)$", message = "URL 형식에 맞추어 작성해주세요")
        String imageUrl,
        @Min(1)
        Long categoryId,
        @Size(min = 1, message = "최소 1이상의 옵션이 필요합니다.")
        List<Long> optionIds
    ){

    }

    public record ProductResponse(
        Long id,
        String name,
        Long price,
        String imageUrl,
        Long categoryId,
        List<Long> optionIds
    ) {
        public static ProductResponse from(ProductEntity productEntity) {
            return new ProductResponse(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.getCategoryEntity().getId(),
                productEntity.getOptionEntities().stream().map(OptionEntity::getId).collect(Collectors.toList())
            );
        }
    }

}

