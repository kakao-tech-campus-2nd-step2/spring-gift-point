package gift.Model;

public enum Role {
    CONSUMER("ROLE_CONSUMER"),
    ADMIN("ROLE_ADMIN");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
