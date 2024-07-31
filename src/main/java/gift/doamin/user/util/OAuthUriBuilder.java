package gift.doamin.user.util;

import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;

public abstract class OAuthUriBuilder {

    KakaoClientProperties clientProperties;
    KakaoProviderProperties providerProperties;

    public OAuthUriBuilder clientProperties(KakaoClientProperties clientProperties) {
        this.clientProperties = clientProperties;
        return this;
    }

    ;

    public OAuthUriBuilder providerProperties(KakaoProviderProperties providerProperties) {
        this.providerProperties = providerProperties;
        return this;
    }

    ;

    abstract public String build();

}
