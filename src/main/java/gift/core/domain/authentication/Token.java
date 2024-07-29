package gift.core.domain.authentication;

public record Token(
        String value
) {
    public static Token of(String value) {
        return new Token(value);
    }
}
