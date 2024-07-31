package gift.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.http.HttpStatus;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiResponse<T> {

    private HttpResult httpResult;
    private String message;
    private HttpStatus httpStatus;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(HttpResult httpResult, String message,
        HttpStatus httpStatus, T data) {
        this.httpResult = httpResult;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public HttpResult getHttpResult() {
        return httpResult;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public T getData() {
        return data;
    }

    public enum HttpResult{
        OK, ERROR
    }
}
