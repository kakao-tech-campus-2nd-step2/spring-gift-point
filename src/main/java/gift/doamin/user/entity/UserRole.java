package gift.doamin.user.entity;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_SELLER"),
    USER("ROLE_USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
