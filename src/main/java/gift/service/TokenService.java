package gift.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import gift.exception.InvalidTokenFormatException;

@Service
public class TokenService {

	private final Map<String, TokenHandler> tokenParsers = new HashMap<>();

    public void addTokenParser(TokenHandler handler) {
        tokenParsers.put(handler.getTokenSuffix(), handler);
    }

    public String extractionEmail(String token) {
    	String accessToken = removeTokenSuffix(token);
    	String tokenType = decisionTokenType(token);
    	return tokenParsers.get(tokenType).parseToken(accessToken);
    }
    
    public String removeTokenSuffix(String tokenWithSuffix) {
        String tokenType = decisionTokenType(tokenWithSuffix);
        return tokenWithSuffix.replace(tokenType, "");
    }

    private String decisionTokenType(String token) {
        return tokenParsers.keySet().stream()
                .filter(suffix -> token.endsWith(suffix))
                .findFirst()
                .orElseThrow(() -> new InvalidTokenFormatException("Invalid token format."));
    }
}
