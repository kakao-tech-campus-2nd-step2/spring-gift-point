package gift.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductDTO (int categoryId,
                     int price,
                     @NotBlank(message = "상품 이름은 비어있을 수 없습니다")
                     @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다")
                     @Pattern(regexp = "[ㄱ-힣\\w\\s()\\[\\]\\+\\-\\&\\/]*", message = "허용되지 않는 특수 문자가 포함되어 있습니다")
                     @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
                     String name,
                     String imageUrl) {}