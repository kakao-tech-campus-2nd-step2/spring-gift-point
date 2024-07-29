package gift.exceptions;

import gift.common.ErrorMessage;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static CustomException invalidNameException() {
        return new CustomException("카카오는 이름에 포함할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    public static CustomException redundantEmailException() {
        return new CustomException(ErrorMessage.emailAlreadyExists, HttpStatus.UNAUTHORIZED);
    }

    public static CustomException userNotFoundException() {
        return new CustomException(ErrorMessage.emailNotExists, HttpStatus.UNAUTHORIZED);
    }

    public static CustomException invalidPasswordException() {
        return new CustomException(ErrorMessage.passwordInvalid, HttpStatus.UNAUTHORIZED);
    }

    public static CustomException invalidTokenException() {
        return new CustomException(ErrorMessage.tokenInvalid, HttpStatus.UNAUTHORIZED);
    }

    public static CustomException invalidHeaderException() {
        return new CustomException(ErrorMessage.headerInvalid, HttpStatus.BAD_REQUEST);
    }

    public static CustomException categoryNotFoundException() {
        return new CustomException(ErrorMessage.categoryNotExists, HttpStatus.BAD_REQUEST);
    }

    public static CustomException productNotFoundException() {
        return new CustomException(ErrorMessage.productNotExists, HttpStatus.BAD_REQUEST);
    }

    public static CustomException optionNotFoundException() {
        return new CustomException(ErrorMessage.optionNotExists, HttpStatus.BAD_REQUEST);
    }

    public static CustomException quantityInvalidException() {
        return new CustomException(ErrorMessage.quantityInvalid, HttpStatus.BAD_REQUEST);
    }

    public static CustomException invalidAPIException(HttpStatus httpStatus) {
        return new CustomException(ErrorMessage.apiInvalid, httpStatus);
    }
}
