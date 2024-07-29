package gift.global.validate;

import java.util.regex.Pattern;

public class ProductNameValidator {


    private static final int MAX_LENGTH = 15;
    private static final Pattern VALID_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$");
    private static final String FORBIDDEN_WORD = "카카오";

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 필수 입력값입니다.");
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 최대 15자까지 입력 가능합니다.");
        }

        if (!VALID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "상품 이름은 영문, 한글, 숫자, 공백,  ( ), [ ], +, -, &, /, _만 입력 가능합니다.");
        }

        if (value.contains(FORBIDDEN_WORD)) {
            throw new IllegalArgumentException(
                " '카카오' 가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다. 따로 문의 부탁드립니다.");
        }

        return true;
    }
}
