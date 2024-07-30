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
	
	public String extractionEmail(String tokenWithSuffix) {
		String tokenType = decisionTokenType(tokenWithSuffix);
		String token = extractioAccessToekn(tokenWithSuffix, tokenType);
		return tokenParsers.get(tokenType).parseToken(token);
	}
	
	private String decisionTokenType(String tokenWithSuffix) {
		return tokenParsers.keySet().stream()
				.filter(suffix -> tokenWithSuffix.endsWith(suffix))
				.findFirst()
				.orElseThrow(() -> new InvalidTokenFormatException("Invalid token format."));
	}
	
	private String extractioAccessToekn(String tokenWithSuffix, String tokenType) {
		return tokenWithSuffix.replace(tokenType, "");
	}
}
