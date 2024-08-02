package gift.api.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.category.domain.Category;
import gift.api.product.domain.Product;
import gift.api.product.validator.NoKakao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductRequest (
    @NotNull(message = "Category id is mandatory")
    @Positive(message = "Category id must be greater than zero")
    Long categoryId,
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 15, message = "Must be at least 1 character, no more than 15 characters long")
    @Pattern(regexp = "^[\\w\\s가-힣()\\[\\]+\\-&/]+$", message = "Only (), [], +, -, &, / of special characters available")
    @NoKakao
    String name,
    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    Integer price,
    @NotBlank(message = "Image url is mandatory")
    String imageUrl
) {
    public Product toEntity(Category category) {
        return new Product(category, name, price, imageUrl);
    }
}