package gift.core.domain.authentication;

public interface AuthenticationService {

    Token authenticate(String principal, String credentials);

}
