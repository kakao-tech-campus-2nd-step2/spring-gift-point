package gift.controller.response;

import org.springframework.http.HttpStatus;

public class ApiResponseBody<D> {

    private final Integer status;
    private final String message;
    private final D data;

    public ApiResponseBody(HttpStatus status, String message, D data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public D getData() {
        return data;
    }
}


