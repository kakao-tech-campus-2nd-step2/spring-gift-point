package gift.product.dto;

import gift.category.entity.Category;
import gift.product.entity.Option;
import gift.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;


public record ProductRequestDto(
    @NotBlank(message = "이름을 입력해야 합니다.")
    @Size(max = 15, message = "이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[\\w \\[\\]\\(\\)\\+\\-\\&\\/\\_가-힣]*$", message = "이름에는 특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "이름에 '카카오' 포함은 불가합니다. 예외적 사용 시 담당 MD의 사전 승인이 필수입니다.")
    String name,

    @NotNull(message = "가격을 입력해야 합니다.")
    @Min(value = 1, message = "가격은 양수여야 합니다.")
    int price,

    @NotBlank(message = "이미지 URL을 입력해야 합니다.")
    String imageUrl,

    @NotNull(message = "카테고리를 입력해야 합니다.")
    Long categoryId
) {

}
