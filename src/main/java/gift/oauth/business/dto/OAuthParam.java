package gift.oauth.business.dto;

import gift.global.domain.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthParam {

    OAuthProvider oAuthProvider();

    MultiValueMap<String, String> getTokenRequestBody();

    MultiValueMap<String, String> getEmailRequestBody();
}
