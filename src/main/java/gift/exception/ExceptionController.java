package gift.exception;

import gift.exception.customException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResult errorResult = new ErrorResult("400", "잘못된 요청입니다.");

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorResult.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResult> entityNotFoundExceptionHandler(EntityNotFoundException e){
        ErrorResult errorResult = new ErrorResult("404", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(KakaoInNameException.class)
    public ResponseEntity<ErrorResult> kakaoInNameHandler(KakaoInNameException e){
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResult> memberNotFoundExceptionHandler(MemberNotFoundException e) {
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(EmailDuplicationException.class)
    public ResponseEntity<ErrorResult> EmailDuplicationHandler(EmailDuplicationException e){
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UnAuthorizationException.class)
    public ResponseEntity<ErrorResult> unAuthorizationExceptionHandler(UnAuthorizationException e) {
        ErrorResult errorResult = new ErrorResult("401", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResult> forbiddenExceptionHandler(ForbiddenException e) {
        ErrorResult errorResult = new ErrorResult("403", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler(NameDuplicationException.class)
    public ResponseEntity<ErrorResult> nameExceptionHandler(NameDuplicationException e) {
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(DenyDeleteException.class)
    public ResponseEntity<ErrorResult> denyDeleteExceptionHandler(DenyDeleteException e) {
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(OptionQuantityNotMinusException.class)
    public ResponseEntity<ErrorResult> optionQuantityHandler(OptionQuantityNotMinusException e){
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ToMuchPointException.class)
    public ResponseEntity<ErrorResult> toMuchPointHandler(ToMuchPointException e){
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResult> httpClientHandler(HttpClientErrorException e){
        ErrorResult errorResult = new ErrorResult(e.getStatusCode().toString(), e.getMessage());
        return new ResponseEntity<>(errorResult, e.getStatusCode());
    }
    @ResponseBody
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResult> httpServerHandler(HttpServerErrorException e){
        ErrorResult errorResult = new ErrorResult(e.getStatusCode().toString(), e.getMessage());
        return new ResponseEntity<>(errorResult, e.getStatusCode());
    }

    @ResponseBody
    @ExceptionHandler(JsonException.class)
    public ResponseEntity<ErrorResult> jsonProcessingHandler(JsonException e){
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> errorHandler(Exception e) {
        ErrorResult errorResult = new ErrorResult("500", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
