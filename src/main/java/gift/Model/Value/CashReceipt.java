package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class CashReceipt {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^01[0-9]-\\d{4}-\\d{4}$"
    );

    private String phoneNumber;

    private CashReceipt() {}

    public CashReceipt(String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        this.phoneNumber = phoneNumber;
    }

    private void validatePhoneNumber(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("phoneNumber 값은 필수입니다");
        if(!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches())
            throw new IllegalArgumentException("phoneNumber의 양식을 다시 확인해 주세요");

        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof CashReceipt))
            return false;

        CashReceipt cashReceipt = (CashReceipt) object;
        return Objects.equals(this.phoneNumber, cashReceipt.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
