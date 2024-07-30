package gift.service;

public interface TokenHandler {

	String parseToken(String token);
	String getTokenSuffix();
}
