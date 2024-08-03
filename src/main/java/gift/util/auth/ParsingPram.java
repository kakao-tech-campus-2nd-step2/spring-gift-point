package gift.util.auth;

import gift.entity.enums.SocialType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParsingPram {


    private final JwtToken jwtToken;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    public ParsingPram(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Long getId(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        token = token.substring(7);
        return jwtToken.getId(token);
    }

    public String getEmail(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        token = token.substring(7);
        return jwtToken.getEmail(token);
    }

    public String getSocialToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        token = token.substring(7);
        return jwtToken.getSocialToken(token);
    }

    public SocialType getSocialType(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        token = token.substring(7);
        return jwtToken.getSocialType(token);
    }
}
