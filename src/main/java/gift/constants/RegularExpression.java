package gift.constants;

public class RegularExpression {

    public static final String PRODUCT_NAME_CHAR_VALID_REGEX = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$";
    public static final String PRODUCT_NAME_FIND_KAKAO_REGEX = "^(?!.*카카오).*$";

    public static final String MEMBER_PASSWORD_VALID_REGEX = "^[a-zA-Z0-9]*$";
    public static final String OPTION_NAME_CHAR_VALID_REGEX = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$";
}
