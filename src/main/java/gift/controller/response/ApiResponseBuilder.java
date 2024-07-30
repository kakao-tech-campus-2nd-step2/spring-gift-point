package gift.controller.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder<T> {

    private HttpHeaders headers;
    private HttpStatus status;
    private String messages;
    private T data;

    public ApiResponseBuilder() {}

    public ApiResponseBuilder<T> headers(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public ApiResponseBuilder<T> httpStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ApiResponseBuilder<T> messages(String messages) {
        this.messages = messages;
        return this;
    }

    public ApiResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResponseEntity<ApiResponseBody<T>> build() {
        return ResponseEntity.status(status).headers(headers).body(new ApiResponseBody<>(status, messages, data));
    }
}