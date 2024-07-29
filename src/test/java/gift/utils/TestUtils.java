package gift.utils;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

public final class TestUtils {

    private TestUtils() {
    }

    public static RequestEntity<Object> createRequestEntity(String url, Object body, HttpMethod method, String accessToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return new RequestEntity<>(body, headers, method, URI.create(url));
    }
}
