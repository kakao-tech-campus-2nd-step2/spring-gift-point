package gift.model.valueObject;

import javax.naming.AuthenticationException;

public class BearerToken {
    private final String token;

    public BearerToken(String token) throws AuthenticationException {
        if (token != null && token.startsWith("Bearer ")) {
            this.token = token.substring(7);
        } else {
            throw new AuthenticationException("헤더 혹은 토큰이 유효하지 않습니다.");
        }
    }

    public String getToken() {
        return token;
    }
}
