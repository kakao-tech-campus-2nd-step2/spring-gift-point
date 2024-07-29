package gift.core.domain.user;

public record User(
        Long id,
        String name,
        UserAccount account
) {
    public User(String name, UserAccount account) {
        this(0L, name, account);
    }
}
