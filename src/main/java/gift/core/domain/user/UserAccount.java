package gift.core.domain.user;

public class UserAccount {
    private final String principal;
    private final String credentials;

    public UserAccount(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public String principal() {
        return principal;
    }

    public String credentials() {
        return credentials;
    }
}