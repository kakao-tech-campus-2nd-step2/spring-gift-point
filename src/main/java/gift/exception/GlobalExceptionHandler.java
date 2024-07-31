package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<String> handleInvalidProductException(InvalidProductException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException e) {
    	return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleInvalidUserException(UnauthorizedException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleInvalidUserException(UserNotFoundException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<String> handleInvalideCategoryException(InvalidCategoryException e){
    	return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
    
    @ExceptionHandler(DuplicateOptionException.class)
    public ResponseEntity<String> handleDuplicateOptionException(DuplicateOptionException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DuplicateCategoryNameException.class)
    public ResponseEntity<String> handleDuplicateCategoryNameException(DuplicateCategoryNameException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidOptionException.class)
    public ResponseEntity<String> handleOptionQuantityException(InvalidOptionException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(KakaoNotEnabledException.class)
    public ResponseEntity<String> handleKakaoNotEnabledException(KakaoNotEnabledException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(InvalidTokenFormatException.class)
    public ResponseEntity<String> handleInvalidTokenFormatException(InvalidTokenFormatException e){
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
