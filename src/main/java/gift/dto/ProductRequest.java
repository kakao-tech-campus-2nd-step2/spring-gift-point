package gift.dto;

import gift.constants.ErrorMessage;
import gift.constants.RegularExpression;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProductRequest {

    private Long id;

    @NotBlank(message = ErrorMessage.PRODUCT_NAME_VALID_NOT_BLANK_MSG)
    @Size(min = 1, max = 15, message = ErrorMessage.PRODUCT_NAME_VALID_SIZE_MSG)
    @Pattern(
        regexp = RegularExpression.PRODUCT_NAME_CHAR_VALID_REGEX,
        message = ErrorMessage.PRODUCT_NAME_VALID_CHAR_MSG)
    @Pattern(
        regexp = RegularExpression.PRODUCT_NAME_FIND_KAKAO_REGEX,
        message = ErrorMessage.PRODUCT_NAME_VALID_KAKAO_MSG)
    private String name;

    @NotNull
    private long price;

    private String imageUrl;

    @NotNull(message = ErrorMessage.CATEGORY_MUST_BE_SELECTED)
    private Long categoryId;

    @NotBlank(message = ErrorMessage.CATEGORY_MUST_BE_SELECTED)
    private String categoryName;

    @Size(min = 1, message = ErrorMessage.OPTION_MUST_MORE_THAN_ZERO)
    private List<OptionSaveRequest> options;

    protected ProductRequest() {
    }

    public ProductRequest(Long id, String name, long price, String imageUrl, Long categoryId,
        String categoryName, List<OptionSaveRequest> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<OptionSaveRequest> getOptions() {
        return options;
    }
}
