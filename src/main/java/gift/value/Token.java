package gift.value;

public class Token {
    private final String token;

    public Token(String token) {
        this.token = token.replace("Bearer ", "");
    }

    public String getToken() {
        return token;
    }
}
