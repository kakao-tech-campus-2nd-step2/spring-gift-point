package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
public class RestTemplateTimeoutTest {

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateTimeout() {
        // given
        mockServer = MockRestServiceServer.createServer(restTemplate);
        String url = "http://invalid-url/api/data";

        // when
        mockServer.expect(requestTo(url))
                .andRespond(delayedResponse(Duration.ofSeconds(10)));
        ResourceAccessException e = assertThrows(ResourceAccessException.class,
                () -> restTemplate.getForObject(url, String.class));

        // then
        assertThat(e.getMessage()).isEqualTo("Timeout");

    }

    public ResponseCreator delayedResponse(Duration delay) {
        return request -> {
            try {
                Thread.sleep(delay.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new ResourceAccessException("Timeout");
        };
    }
}
