package gift.common.exception;

import org.springframework.http.HttpStatusCode;

public class CustomClientErrorException extends RuntimeException{
    private HttpStatusCode code;
    public CustomClientErrorException(HttpStatusCode code, String message) {
        super(message);
        this.code = code;
    }

    public HttpStatusCode getCode() {
        return code;
    }
}
