package gift.authentication.service;

import gift.core.domain.authentication.OAuthType;

public interface OAuthGatewayProvider {

    OAuthGateway getGateway(OAuthType type);

}
