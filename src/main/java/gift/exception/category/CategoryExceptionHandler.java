package gift.exception.category;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateCategoryException.class)
    public ErrorResult duplicateExHandle(DuplicateCategoryException e) {
        return new ErrorResult("카테고리 생성 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundCategoryException.class)
    public ErrorResult notFoundExHandle(NotFoundCategoryException e) {
        return new ErrorResult("카테고리 조회 에러", e.getMessage());
    }
}
