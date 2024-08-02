package gift.exception;

import gift.dto.response.ErrorResponse;
import gift.exception.categoryException.CategoryNotFoundException;
import gift.exception.memberException.DuplicatedEmailException;
import gift.exception.memberException.EmailConstraintViolationException;
import gift.exception.memberException.InvalidLoginException;
import gift.exception.memberException.NormalMemberSignUpException;
import gift.exception.optionException.DuplicatedOptionException;
import gift.exception.optionException.OptionQuantityException;
import gift.exception.orderException.deductPointException;
import gift.exception.productException.ProductNotFoundException;
import gift.exception.wishException.DuplicatedWishException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.PersistenceException;
import jdk.jfr.Description;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
    * @Description("제약 조건 위반 오류가 발생했습니다.")
    @ExceptionHandler(value = NormalMemberSignUpException.class)
    public ResponseEntity<ErrorResponse> handleNormalMemberSignUpException(NormalMemberSignUpException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
    *
    * */


@ControllerAdvice
public class GlobalExceptionHandler {


    // member exception
    @Description("이메일이 중복된 경우")
    @ExceptionHandler(value = DuplicatedEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedEmailException(DuplicatedEmailException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }


    @Description("이메일 제약 조건을 위반한 경우")
    @ExceptionHandler(value = EmailConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleEmailConstraintViolationException(EmailConstraintViolationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @Description("멤버가 존재하지 않는 경우")
    @ExceptionHandler(value = InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(InvalidLoginException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }


    @Description("카카오 로그인 token 오류")
    @ExceptionHandler(value = KakaoLoginException.class)
    public ResponseEntity<ErrorResponse> handleKakaoLoginException(KakaoLoginException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
                .value(), "카카오 로그인 오류.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }


    @Description("제약 조건 위반 오류가 발생했습니다.")
    @ExceptionHandler(value = NormalMemberSignUpException.class)
    public ResponseEntity<ErrorResponse> handleNormalMemberSignUpException(NormalMemberSignUpException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    // category exception


    @Description("카테고리 서비스 exception")
    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryException(CategoryNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }



    // product exception

    @Description("상품을 찾을 수 없을 때 exception")
    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }







    // option exception

    @Description("중복된 옵션 이름")
    @ExceptionHandler(value = DuplicatedOptionException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedOptionException(DuplicatedOptionException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @Description("옵션 서비스 exception")
    @ExceptionHandler(value = OptionQuantityException.class)
    public ResponseEntity<ErrorResponse> handleOptionException(OptionQuantityException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND
                .value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }



    // wish exception

    @ExceptionHandler(value = DuplicatedWishException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedWishException(DuplicatedWishException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }





    // order exception

    @ExceptionHandler(value = deductPointException.class)
    public ResponseEntity<ErrorResponse> handledeductPointException(deductPointException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    // jwt exception

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }





    // 기타 exception

    @Description("영속성 오류가 발생했습니다.")
    @ExceptionHandler(value = PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR
                .value(), "영속성 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }



    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }




    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }



}