package gift.product.entity.embedded;

import static gift.exception.ErrorMessage.PRODUCT_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.PRODUCT_NAME_KAKAO_STRING;
import static gift.exception.ErrorMessage.PRODUCT_NAME_LENGTH;

import jakarta.persistence.Embeddable;

@Embeddable
public class Name {

    private String value;

    public Name() {
    }

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void update(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        isNameLengthUnder15(value);
        isNameAllowedCharacter(value);
        isNameIncludeKakao(value);

        this.value = value;
    }

    private void isNameAllowedCharacter(String value) {
        if (value.matches("\"^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_]*$\"")) {
            throw new IllegalArgumentException(PRODUCT_NAME_ALLOWED_CHARACTER);
        }
    }

    private void isNameIncludeKakao(String value) {
        if (!value.matches("^(?i)(?!.*(kakao|카카오)).*$")) {
            throw new IllegalArgumentException(PRODUCT_NAME_KAKAO_STRING);
        }
    }

    private void isNameLengthUnder15(String value) {
        if (value.length() > 15) {
            throw new IllegalArgumentException(PRODUCT_NAME_LENGTH);
        }
    }
}
