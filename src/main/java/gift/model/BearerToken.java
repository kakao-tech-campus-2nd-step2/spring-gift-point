package gift.model;

import javax.naming.AuthenticationException;

public class BearerToken {
    private final String token;

    public BearerToken(String token) throws AuthenticationException {
        this.token = parseToken(token);
    }

    public String parseToken(String token) throws AuthenticationException{
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        else{
            throw new AuthenticationException("헤더 혹은 토큰이 유효하지 않습니다.");
        }
    }

    public String getToken() {
        return token;
    }
}
