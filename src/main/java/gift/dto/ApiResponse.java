package gift.dto;

import gift.model.HttpResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private HttpResult result;
    private String message;
    private HttpStatus httpStatus;
    private T data;

    public ApiResponse(HttpResult result, String message, HttpStatus httpStatus) {
        this.result = result;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ApiResponse(HttpResult result, T data, HttpStatus httpStatus) {
        this.result = result;
        this.data = data;
        this.httpStatus = httpStatus;
    }
}