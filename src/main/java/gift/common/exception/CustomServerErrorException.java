package gift.common.exception;

import org.springframework.http.HttpStatusCode;

public class CustomServerErrorException extends RuntimeException{

    private HttpStatusCode code;
    public CustomServerErrorException(HttpStatusCode code, String message) {
        super(message);
        this.code = code;
    }

    public HttpStatusCode getCode() {
        return code;
    }
}
