package gift.authentication.service;

import gift.core.domain.authentication.OAuthType;

public interface OAuthGateway {

    OAuthResult authenticate(String code);

    OAuthType getType();

}
