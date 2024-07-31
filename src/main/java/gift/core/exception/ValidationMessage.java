package gift.core.exception;

public enum ValidationMessage {
	NO_KAKAO(ValidationMessage.NO_KAKAO_MSG),
	INVALID_SIZE(ValidationMessage.INVALID_SIZE_MSG),
	INVALID_CHARSET(ValidationMessage.INVALID_CHARSET_MSG),
	NOT_NULL(ValidationMessage.NOT_NULL_MSG),
	NOT_DUPLICATE(ValidationMessage.NOT_DUPLICATE_MSG);

	public static final String NO_KAKAO_MSG = "cannot contain '카카오'";
	public static final String INVALID_SIZE_MSG = "must be at most 15 characters long";
	public static final String INVALID_CHARSET_MSG = "알파벳,한글,숫자,특수문자 ( ), [ ], +, -, &, /, _만 입력 가능합니다.";
	public static final String NOT_NULL_MSG = "category must not be null";
	public static final String NOT_DUPLICATE_MSG = "options must not contain duplicate names";

	private final String message;

	ValidationMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}