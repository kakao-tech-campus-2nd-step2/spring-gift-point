package gift.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WebErrorHandler {
    public static Mono<Throwable> handleErrorResponse(HttpStatusCode statusCode) {
        if (statusCode.is4xxClientError()) {
            return Mono.error(new RuntimeException("Invalid Parameter"));
        }
        if (statusCode.is5xxServerError()) {
            return Mono.error(new RuntimeException("Internal Server Error"));
        }
        return Mono.error(new RuntimeException("Unexpected Error"));
    }
}
