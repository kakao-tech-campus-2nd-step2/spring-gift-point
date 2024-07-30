package gift.global.security;

import gift.global.exception.AccessTokenNotExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Enumeration;

public class AuthorizationExtractor {
    public static final String BEARER_TYPE = "Bearer";

    private AuthorizationExtractor() {
    }

    public static String extractAccessToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
        if (!headers.hasMoreElements()) {
            throw new AccessTokenNotExistsException();
        }
        return extract(headers.nextElement());
    }

    private static String extract(String header) {
        String authHeaderValue = "";

        if (header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            authHeaderValue = header.substring(BEARER_TYPE.length()).trim();
        }

        return authHeaderValue;
    }
}
