package gift.option.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.option.exception.OptionValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OptionQuantity {
    public static final long MIN_COUNT = 1L;
    public static final long MAX_COUNT = 100_000_000L;

    @Column(name = "quantity")
    private Long optionCountValue;

    public OptionQuantity() {
    }

    public OptionQuantity(Long optionCountValue) {
        if (optionCountValue == null || optionCountValue < MIN_COUNT || optionCountValue >= MAX_COUNT) {
            throw new OptionValidException(ErrorCode.OPTION_COUNT_OUTBOUND_ERROR);
        }
        this.optionCountValue = optionCountValue;
    }

    @JsonValue
    public Long getOptionCountValue() {
        return optionCountValue;
    }

    @Override
    public String toString() {
        return Long.toString(optionCountValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionQuantity that = (OptionQuantity) o;
        return Objects.equals(optionCountValue, that.optionCountValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionCountValue);
    }
}
