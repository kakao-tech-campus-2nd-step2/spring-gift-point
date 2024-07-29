package gift.resolver;

import gift.exception.ErrorCode;
import gift.exception.GiftException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthHeaderManager {

    private final String AUTHORIZATION = "Authorization";
    private final String PREFIX = "Bearer ";

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX)) {
            throw new GiftException(ErrorCode.MISSING_TOKEN);
        }

        return header.replace(PREFIX, "");
    }

}
