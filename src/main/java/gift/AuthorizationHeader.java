package gift;

public class AuthorizationHeader {
    private final String token;


    public AuthorizationHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {

            this.token = header.substring(7);
        } else {
            this.token = null;
        }
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return token != null;
    }
}