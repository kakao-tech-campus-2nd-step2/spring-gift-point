package gift.authentication.service;

import gift.core.domain.authentication.*;
import org.springframework.stereotype.Service;

@Service
public class OAuthServiceImpl implements OAuthService {
    private final OAuthGatewayProvider gatewayProvider;
    public OAuthServiceImpl(
            OAuthGatewayProvider gatewayProvider
    ) {
        this.gatewayProvider = gatewayProvider;
    }

    @Override
    public Token authenticate(OAuthType type, String token) {
        OAuthGateway gateway = gatewayProvider.getGateway(type);
        OAuthResult result = gateway.authenticate(token);

        return Token.of(result.accessToken());
    }
}
