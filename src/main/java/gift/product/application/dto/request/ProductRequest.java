package gift.product.application.dto.request;

import gift.common.validation.NamePattern;
import gift.common.validation.ValidateErrorMessage;
import gift.product.service.command.ProductCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public record ProductRequest(
        @NamePattern
        @NotBlank(message = ValidateErrorMessage.INVALID_PRODUCT_NAME_NULL)
        @Length(max = 15, message = ValidateErrorMessage.INVALID_PRODUCT_NAME_LENGTH)
        String name,

        @NotNull(message = ValidateErrorMessage.INVALID_PRODUCT_PRICE_NULL)
        @Range(min = 1, max = 2_100_000_000, message = ValidateErrorMessage.INVALID_PRODUCT_PRICE_RANGE)
        Integer price,

        @NotBlank(message = ValidateErrorMessage.INVALID_PRODUCT_IMG_URL_NULL)
        @URL(message = ValidateErrorMessage.INVALID_PRODUCT_IMG_URL_FORMAT)
        String imgUrl,

        @NotBlank(message = ValidateErrorMessage.INVALID_CATEGORY_NAME_NULL)
        String categoryName
) {
    public ProductCommand toProductCommand() {
        return new ProductCommand(name(), price(), imgUrl(), categoryName());
    }
}