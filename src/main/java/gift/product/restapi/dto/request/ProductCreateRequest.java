package gift.product.restapi.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.product.Product;
import gift.product.restapi.validator.NotContainingKaKao;
import jakarta.validation.constraints.*;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductCreateRequest(
        @Size(min = 1, max = 100, message = "상품명은 1자 이상 100자 이하여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "특수문자는 ()[]+-&/_ 만 허용됩니다.")
        @NotContainingKaKao
        String name,
        @NotNull @Positive Integer price,
        @NotBlank String imageUrl,
        @NotBlank String categoryName
) {
    public Product toDomain() {
        return new Product(null, name, price, imageUrl, null);
    }
}
