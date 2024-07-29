package gift.product;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class ResponseDTO {
    HttpStatus HttpStatus;
    String message;
    Object details;

    public ResponseDTO() {
    }

    public ResponseDTO(HttpStatus HttpStatus, String message, Object details) {
        this.HttpStatus = HttpStatus;
        this.message = message;
        this.details = details;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus;
    }

    public void setHttpStatus(HttpStatus HttpStatus) {
        this.HttpStatus = HttpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(HashMap<?, ?> details) {
        this.details = details;
    }
}
