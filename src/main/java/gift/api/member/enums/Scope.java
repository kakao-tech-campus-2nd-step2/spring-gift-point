package gift.api.member.enums;

public enum Scope {
    MESSAGE("talk_message"),
    EMAIL("account_email");

    private final String id;

    Scope(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
