package gift.exception;

public enum ErrorCode {
    MEMBER_NOT_FOUND("해당 사용자를 데이터 베이스에서 찾을 수 없습니다.\nEmail: %s\nPassword: %s"),

    MEMBER_ID_NOT_FOUND("해당 사용자를 데이터 베이스에서 찾을 수 없습니다.\nMember ID: %d"),
    PRODUCT_NOT_FOUND("해당 상품 데이터 베이스에서 찾을 수 없습니다.\nProduct ID: %d"),
    WISHLIST_NOT_FOUND("Member ID = %d 사용자의 위시 리스트에서 Product Id = %d 을(를) 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("해당 카테고리를 데이터 베이스에서 찾을 수 없습니다\nCategory Id = %d"),
    OPTION_NOT_FOUND("해당 옵션을 데이터 베이스에서 찾을 수 없습니다\nOption Id = %d");

    private final String messageTemplate;

    ErrorCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String formatMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
