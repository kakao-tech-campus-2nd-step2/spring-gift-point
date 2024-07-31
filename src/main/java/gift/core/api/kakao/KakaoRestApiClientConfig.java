package gift.core.api.kakao;

import java.time.Duration;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class KakaoRestApiClientConfig {

	@Bean
	public KakaoRestClient githubRestApiClient() {
		ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
			.withReadTimeout(Duration.ofSeconds(10));

		ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

		RestClient restClient = RestClient.builder()
			.requestFactory(requestFactory)
			.build();
		RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
		return factory.createClient(KakaoRestClient.class);
	}
}