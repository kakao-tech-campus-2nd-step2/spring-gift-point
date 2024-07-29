package gift.exception.option;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OptionsExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundOptionsException.class)
    public ErrorResult notFoundExHandle(NotFoundOptionsException e) {
        return new ErrorResult("상품 옵션 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateOptionsException.class)
    public ErrorResult duplicateExHandle(DuplicateOptionsException e) {
        return new ErrorResult("상품 옵션 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeleteOptionsException.class)
    public ErrorResult deleteExHandle(DeleteOptionsException e) {
        return new ErrorResult("상품 옵션 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OptionsQuantityException.class)
    public ErrorResult optionsQuantityExHandle(OptionsQuantityException e) {
        return new ErrorResult("상품 옵션 수량 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FailedRetryException.class)
    public ErrorResult failedRetryExHandle(FailedRetryException e) {
        return new ErrorResult("상품 옵션 수량 에러", e.getMessage());
    }
}
