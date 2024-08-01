package gift.web.validation.exception.code;

public enum Category {

    COMMON("common"),
    AUTHENTICATION("authentication"),
    AUTHORIZATION("authorization"),
    POLICY("policy"),
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
