package gift.global.handler;

import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.option.OptionOnlyOneDeleteException;
import gift.global.exception.restTemplate.RestTemplateException;
import gift.global.exception.wish.WishNotFoundException;
import gift.global.exception.category.CategoryDuplicateException;
import gift.global.exception.category.CategoryNotFoundException;
import gift.global.exception.option.OptionDuplicateException;
import gift.global.exception.option.OptionNotFoundException;
import gift.global.exception.product.ProductDuplicateException;
import gift.global.exception.product.ProductNotFoundException;
import gift.global.exception.restTemplate.RestTemplateClientException;
import gift.global.exception.restTemplate.RestTemplateServerException;
import gift.global.exception.user.UserDuplicateException;
import gift.global.exception.user.MemberNotFoundException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResponseMaker;
import io.jsonwebtoken.JwtException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * RuntimeException 을 상속받는 커스텀 에러 핸들러
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST,
            e.getMessage()); // TODO 수정 필요
    }


    /**
     * MethodArgumentNotValidException 에러 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> MethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        String message = String.join("\n", errors);
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, message);
    }

    /**
     * JwtException 에러 핸들러
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> JwtException(JwtException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * DB 참조 무결성 에러 핸들러
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleJdbcSQLIntegrityConstraintViolationException(
        DataIntegrityViolationException e
    ) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, "해당 카테고리를 사용하는 상품 존재");
    }

    /**
     * RestTemplate 관련 에러 핸들러
     */
    @ExceptionHandler(RestTemplateException.class)
    public ResponseEntity<ErrorResponseDto> RestTemplateException(RestTemplateException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR,
            "예상치 못한 문제가 발생했습니다. " + e.getMessage());
    }

    @ExceptionHandler(RestTemplateClientException.class)
    public ResponseEntity<ErrorResponseDto> RestTemplateClientException(
        RestTemplateClientException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST,
            "카카오톡 메시지 전송에 실패했습니다. " + e.getMessage());
    }

    @ExceptionHandler(RestTemplateServerException.class)
    public ResponseEntity<ErrorResponseDto> RestTemplateServerException(
        RestTemplateServerException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR,
            "카카오톡 서버에 문제가 발생했습니다. " + e.getMessage());
    }

    /**
     * 도메인별 DUPLICATE_EXCEPTION
     */
    @ExceptionHandler(ProductDuplicateException.class)
    public ResponseEntity<ErrorResponseDto> ProductDuplicateException(ProductDuplicateException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CategoryDuplicateException.class)
    public ResponseEntity<ErrorResponseDto> CategoryDuplicateException(
        CategoryDuplicateException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OptionDuplicateException.class)
    public ResponseEntity<ErrorResponseDto> OptionDuplicateException(OptionDuplicateException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<ErrorResponseDto> UserDuplicateException(UserDuplicateException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 도메인별 NOT_FOUND_EXCEPTION
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> ProductNotFoundException(ProductNotFoundException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> CategoryNotFoundException(CategoryNotFoundException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> OptionNotFoundException(OptionNotFoundException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> UserNotFoundException(MemberNotFoundException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(WishNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> WishNotFoundException(WishNotFoundException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.NOT_FOUND, e.getMessage());
    }

    /**
     * Option 삭제 관련 에러
     */
    @ExceptionHandler(OptionOnlyOneDeleteException.class)
    public ResponseEntity<ErrorResponseDto> OptionOnlyOneDeleteException(
        OptionOnlyOneDeleteException e) {
        return ResponseMaker.createErrorResponse(ErrorCode.BAD_REQUEST, e.getMessage());
    }
}
