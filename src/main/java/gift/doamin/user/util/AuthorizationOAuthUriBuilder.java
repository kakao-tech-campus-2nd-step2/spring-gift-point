package gift.doamin.user.util;

public class AuthorizationOAuthUriBuilder extends OAuthUriBuilder {

    @Override
    public String build() {
        StringBuilder uri = new StringBuilder();
        uri.append(providerProperties.authorizationUri());
        uri.append("?");
        uri.append("scope=" + String.join(",", clientProperties.scope()));
        uri.append("&response_type=code");
        uri.append("&redirect_uri=" + clientProperties.redirectUri());
        uri.append("&client_id=" + clientProperties.clientId());

        return uri.toString();
    }
}
