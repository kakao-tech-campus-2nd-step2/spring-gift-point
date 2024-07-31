package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import gift.exception.RestTemplateResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

	@Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler errorHandler) {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		restTemplate.setErrorHandler(errorHandler);
		return restTemplate;
    }
	
	private ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(5000);
		factory.setReadTimeout(5000);
		return factory;
	}
}
