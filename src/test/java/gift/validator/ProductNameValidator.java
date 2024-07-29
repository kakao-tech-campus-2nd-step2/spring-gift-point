package gift.validator;

import gift.exception.ErrorCode;
import gift.exception.InvalidProductNameException;
import java.util.regex.Pattern;

public class ProductNameValidator {
    public static void validateProductName(String name) {
        if (name.length() > 20) {
            throw new InvalidProductNameException(ErrorCode.INVALID_NAME_LENGTH);
        }
        if (!Pattern.matches("[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*", name)) {
            throw new InvalidProductNameException(ErrorCode.INVALID_CHARACTERS);
        }
        if (name.contains("카카오")) {
            throw new InvalidProductNameException(ErrorCode.CONTAINS_KAKAO);
        }
    }
}
