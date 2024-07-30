package gift.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
public class LinkedMultiValueMapTest {

    private MockWebServer mockWebServer;

    private String mockWebServerUrl;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServerUrl = mockWebServer.url("/MapTest").toString();
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testMap() {
        RestClient client = RestClient.builder().build();

        assertThrows(RestClientException.class,
            () -> client.post()
                .uri(URI.create(mockWebServerUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(mapbody()) // request body
                .retrieve()
                .toEntity(String.class)
        );
    }

    @Test
    void testLMVM() {
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{}")
            .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED));

        RestClient client = RestClient.builder().build();
        var response = client.post()
            .uri(URI.create(mockWebServerUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LMVMbody()) // request body
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getBody()).isEqualTo("{}");
    }

    private LinkedMultiValueMap<String, String> LMVMbody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "client_id");
        body.add("redirect_uri", "redirect_uri");
        body.add("code", "code");

        return body;
    }

    private Map<String, String> mapbody() {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("client_id", "client_id");
        body.put("redirect_uri", "redirect_uri");
        body.put("code", "code");

        return body;
    }

}
