package gift.core.domain.authentication;

public interface OAuthService {

    Token authenticate(OAuthType type, String token);

}
