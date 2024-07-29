package gift.service;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ClientInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("ClientInterceptor start");
        ClientHttpResponse response = execution.execute(request, body);

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new IOException("API HttpRequest error" + response.getStatusCode());
        }
        System.out.println("ClientInterceptor response : " + response.getStatusCode());
        return response;
    }
}
