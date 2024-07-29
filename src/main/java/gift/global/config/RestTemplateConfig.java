package gift.global.config;

import gift.global.exception.restTemplate.RestTemplateException;
import gift.global.exception.restTemplate.RestTemplateClientException;
import gift.global.exception.restTemplate.RestTemplateServerException;
import java.time.Duration;
import java.util.Collections;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(3)) // 연결
            .setReadTimeout(Duration.ofSeconds(3)) // 읽기
            .additionalInterceptors(clientHttpRequestInterceptor())
            .build();
    }

    /**
     *  RestTemplate 이 HTTP 요청 보낼때마다 요청 가로챔
     */
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return ((request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            // 기본 재시도 정책 - 최대 3번 재시도
            SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, Collections.singletonMap(Exception.class, true));
            // 특정 에러에 대한 재시도 정책
            ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
            retryPolicy.setPolicyMap(Collections.singletonMap(HttpClientErrorException.class, // 4xx
                new SimpleRetryPolicy(1, Collections.singletonMap(HttpClientErrorException.class, false))
            ));
            // 정책 등록
            retryTemplate.setRetryPolicy(retryPolicy);
            retryTemplate.setRetryPolicy(retryPolicy);

            try {
                return retryTemplate.execute(
                    context -> execution.execute(request, body)); // HTTP 요청 실행
            } catch (HttpClientErrorException e) {
                throw new RestTemplateClientException(e.getMessage());
            } catch (HttpServerErrorException e) {
                throw new RestTemplateServerException(e.getMessage());
            } catch (Throwable throwable) {
                throw new RestTemplateException(throwable.getMessage());
            }
        });
    }


}
