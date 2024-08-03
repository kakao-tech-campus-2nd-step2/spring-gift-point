package gift.classes.RequestState;

import org.springframework.http.HttpStatus;

public class RequestStateDTO {

    public HttpStatus status;
    public String message;

    public RequestStateDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
