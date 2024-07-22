package gift.member.domain;

public enum MemberType {
    USER("USER"), ADMIN("ADMIN");

    private final String value;

    MemberType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
