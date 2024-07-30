package gift.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatusCode(){
        return errorCode.getStatusCode();
    }

    public HttpStatus getStatus(){
        return errorCode.getStatus();
    }

}
