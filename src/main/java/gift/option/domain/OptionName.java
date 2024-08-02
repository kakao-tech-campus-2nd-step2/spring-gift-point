package gift.option.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.option.exception.OptionValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class OptionName {
    public static final int MAX_LENGTH = 50;
    public static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-&/_]*$");

    @Column(name = "name")
    private String optionNameValue;

    public OptionName() {
    }

    public OptionName(String optionNameValue) {
        if (Objects.isNull(optionNameValue)) {
            throw new OptionValidException(ErrorCode.OPTION_NAME_LENGTH_ERROR);
        }

        optionNameValue = optionNameValue.trim();

        if (optionNameValue.isEmpty() || optionNameValue.length() > MAX_LENGTH) {
            throw new OptionValidException(ErrorCode.OPTION_NAME_LENGTH_ERROR);
        }

        if (!PATTERN.matcher(optionNameValue).matches()) {
            throw new OptionValidException(ErrorCode.OPTION_NAME_PATTER_ERROR);
        }
        this.optionNameValue = optionNameValue;
    }

    public String getOptionNameValue() {
        return optionNameValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return optionNameValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionName that = (OptionName) o;
        return Objects.equals(optionNameValue, that.optionNameValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionNameValue);
    }
}
