package gift.product.application.dto.request;

import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_NAME_LENGTH;
import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_NAME_NULL;
import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_QUANTITY_NULL;
import static gift.common.validation.ValidateErrorMessage.INVALID_OPTION_QUANTITY_RANGE;

import gift.common.validation.NamePattern;
import gift.product.service.command.ProductOptionCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record ProductOptionRequest(
        @NamePattern
        @NotBlank(message = INVALID_OPTION_NAME_NULL)
        @Length(max = 50, message = INVALID_OPTION_NAME_LENGTH)
        String name,

        @NotNull(message = INVALID_OPTION_QUANTITY_NULL)
        @Range(min = 1, max = 99_999_999, message = INVALID_OPTION_QUANTITY_RANGE)
        Integer quantity
) {
    public ProductOptionCommand toProductOptionCommand() {
        return new ProductOptionCommand(name(), quantity());
    }
}
