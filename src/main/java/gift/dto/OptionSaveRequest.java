package gift.dto;

import gift.constants.ErrorMessage;
import gift.constants.RegularExpression;
import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionSaveRequest {

    @NotBlank(message = ErrorMessage.OPTION_NAME_NOT_BLANK_MSG)
    @Size(min = 1, max = 50, message = ErrorMessage.OPTION_NAME_INVALID_LENGTH_MSG)
    @Pattern(regexp = RegularExpression.OPTION_NAME_CHAR_VALID_REGEX, message = ErrorMessage.OPTION_NAME_INVALID_PATTERN_MSG)
    private String name;

    @Min(value = 1, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    @Max(value = 99_999_999, message = ErrorMessage.OPTION_QUANTITY_INVALID_MSG)
    private int quantity;

    @NotNull(message = ErrorMessage.NULL_POINTER_EXCEPTION_MSG)
    private Long productId;

    protected OptionSaveRequest() {
    }

    public OptionSaveRequest(String name, int quantity, Long productId) {
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public Option toEntity(Product product) {
        return new Option(name, quantity, product);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
