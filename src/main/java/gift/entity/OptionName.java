package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Embeddable
public class OptionName {

    private static final int MAX_NAME_LENGTH = 50;
    private static final String NAME_PATTERN = "^[\\가-힣\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$";
    private static final String NULL_NAME_ERROR_MESSAGE = "옵션 이름은 null 일 수 없습니다.";
    private static final String MAX_LENGTH_ERROR_MESSAGE = "옵션 이름은 50자 이하여야합니다. (공백포함)";
    private static final String PATTERN_ERROR_MESSAGE = "( ), [ ], +, -, &, /, _ 외 특수문자는 사용 불가능합니다.";

    @NotNull(message = NULL_NAME_ERROR_MESSAGE)
    @Size(max = MAX_NAME_LENGTH, message = MAX_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = PATTERN_ERROR_MESSAGE)
    @Column(name = "name", nullable = false, length = 50, columnDefinition = "VARCHAR(50) NOT NULL COMMENT 'Option Name'")
    private String value;

    protected OptionName() {
    }

    public OptionName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_OPTION_NAME, NULL_NAME_ERROR_MESSAGE);
        }
        if (value.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(ErrorCode.INVALID_OPTION_NAME, MAX_LENGTH_ERROR_MESSAGE);
        }
        if (!value.matches(NAME_PATTERN)) {
            throw new BusinessException(ErrorCode.INVALID_OPTION_NAME, PATTERN_ERROR_MESSAGE);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
