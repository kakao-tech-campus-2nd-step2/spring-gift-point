package gift.oauth.business.client;

import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;
import gift.oauth.business.dto.OauthToken;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();

    OauthToken.Common getOAuthToken(OAuthParam param);

    String getEmail(String accessToken, OAuthParam param);
}
