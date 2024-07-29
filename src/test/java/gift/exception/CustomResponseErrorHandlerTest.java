package gift.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class CustomResponseErrorHandlerTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    CustomResponseErrorHandler customResponseErrorHandler;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        restTemplate.setErrorHandler(customResponseErrorHandler);
    }

    @Test
    @DisplayName("Test for handler error with client error")
    void handlerError_withClientError() {
        // given
        String url = "http://server-test";
        mockServer.expect(once(), requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .body("Not Found"));

        // when & then
        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        });

        // Verify the exception details
        assert thrown.getStatusCode() == HttpStatus.NOT_FOUND;
        assert thrown.getMessage().contains("Not Found");
    }

    @Test
    @DisplayName("Test for handler error with client error (4xx)")
    void handlerError_withClientError_Generic() {
        // given
        String url = "http://server-test";
        mockServer.expect(once(), requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .body("Bad Request"));

        // when & then
        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        });

        // Verify the exception details
        assert thrown.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert thrown.getMessage().contains("Client Error");
    }

    @Test
    @DisplayName("Test for handler error with server error (500 Internal Server Error)")
    void handlerError_withServerError() {
        // given
        String url = "http://server-test";
        mockServer.expect(once(), requestTo(url))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Internal Server Error"));

        // when & then
        HttpServerErrorException thrown = assertThrows(HttpServerErrorException.class, () -> {
            restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        });

        // Verify the exception details
        assert thrown.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        assert thrown.getMessage().contains("Server Error");
    }

}
