package gift.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    
    HttpStatus httpStatus;
    int code;

    public CustomException(String message, HttpStatus httpStatus, int code){
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }

    public int getCode(){
        return code;
    }
}
