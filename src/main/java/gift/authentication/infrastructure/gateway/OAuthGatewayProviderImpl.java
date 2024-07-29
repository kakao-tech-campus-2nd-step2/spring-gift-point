package gift.authentication.infrastructure.gateway;

import gift.authentication.service.OAuthGateway;
import gift.authentication.service.OAuthGatewayProvider;
import gift.core.domain.authentication.OAuthType;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class OAuthGatewayProviderImpl implements OAuthGatewayProvider {
    private final ObjectProvider<OAuthGateway> gateways;

    public OAuthGatewayProviderImpl(ObjectProvider<OAuthGateway> gateways) {
        this.gateways = gateways;
    }

    @Override
    public OAuthGateway getGateway(OAuthType type) {
        return gateways.stream()
                .filter(gateway -> gateway.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not Supported OAuth Type : " + type));
    }
}
