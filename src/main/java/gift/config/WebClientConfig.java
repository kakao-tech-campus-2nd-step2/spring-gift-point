package gift.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@ConfigurationProperties(prefix = "webclient")
public class WebClientConfig {

    private final int connectTimeoutMillis;
    private final int responseTimeoutSeconds;
    private final int readTimeoutSeconds;
    private final int writeTimeoutSeconds;

    public WebClientConfig(int connectTimeoutMillis, int responseTimeoutSeconds, int readTimeoutSeconds, int writeTimeoutSeconds) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.responseTimeoutSeconds = responseTimeoutSeconds;
        this.readTimeoutSeconds = readTimeoutSeconds;
        this.writeTimeoutSeconds = writeTimeoutSeconds;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis)
                .responseTimeout(Duration.ofSeconds(responseTimeoutSeconds))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(readTimeoutSeconds))
                                .addHandlerLast(new WriteTimeoutHandler(writeTimeoutSeconds)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
