package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class CashReceipt {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^01[0-9]-\\d{4}-\\d{4}$"
    );

    private String value;

    private CashReceipt() {}

    public CashReceipt(String value) {
        validateValue(value);

        this.value= value;
    }

    private void validateValue(String value) {
        if(value == null || value.isBlank())
            throw new IllegalArgumentException("phoneNumber 값은 필수입니다");
        if(!PHONE_NUMBER_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("phoneNumber의 양식을 다시 확인해 주세요");

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof CashReceipt))
            return false;

        CashReceipt cashReceipt = (CashReceipt) object;
        return Objects.equals(this.value, cashReceipt.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
