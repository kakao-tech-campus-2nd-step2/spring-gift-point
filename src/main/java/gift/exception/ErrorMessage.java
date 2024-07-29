package gift.exception;

public class ErrorMessage {

    public static final String MEMBER_NOT_FOUND = "Member not found";
    public static final String MEMBER_ALREADY_EXISTS = "Member already exists";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String PRODUCT_NAME_LENGTH = "product name's length must be between 1 and 15";
    public static final String PRODUCT_NAME_ALLOWED_CHARACTER = "product name must consist of English, Korean, numbers, and special symbols (, ), [, ], +, -, &, /, _";
    public static final String PRODUCT_NAME_KAKAO_STRING = "if you include 'kakao' in you product name, then you must be consult with your MD";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String WISHLIST_ALREADY_EXISTS = "Wishlist already exists";
    public static final String WISHLIST_NOT_FOUND = "Wishlist not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String OPTION_ALREADY_EXISTS = "Option already exists";
    public static final String OPTION_NOT_FOUND = "Option not found";
    public static final String OPTION_NAME_LENGTH = "Option name length must be between 1 and 50 include blank";
    public static final String OPTION_NAME_ALLOWED_CHARACTER = "Option name must consist of English, Korean, numbers, blank and special symbols (, ), [, ], +, -, &, /, _";
    public static final String OPTION_QUANTITY_SIZE = "Option quantity size must be between 1 and 100,000,000";
    public static final String OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER = "Option subtract quantity not allowed negative numbers";
    public static final String KAKAO_AUTHENTICATION_FAILED = "Kakao authentication failed";
    public static final String JSON_CONVERT_FAILED = "Unable to convert JSON";
}
