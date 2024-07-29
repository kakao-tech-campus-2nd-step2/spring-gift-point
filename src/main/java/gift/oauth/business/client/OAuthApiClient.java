package gift.oauth.business.client;

import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;
import gift.oauth.business.dto.OAuthToken;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();

    OAuthToken.Common getOAuthToken(OAuthParam param);

    String getEmail(String accessToken, OAuthParam param);
}
