package gift.controller;

import gift.utils.error.CategoryNotFoundException;
import gift.utils.error.DuplicateOptionException;
import gift.utils.error.KakaoLoginException;
import gift.utils.error.NotpermitNameException;
import gift.utils.error.OptionNameDuplicationException;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.ProductAlreadyExistException;
import gift.utils.error.ProductNotFoundException;
import gift.utils.error.TokenAuthException;
import gift.utils.error.UserAlreadyExistsException;
import gift.utils.error.UserNotFoundException;
import gift.utils.error.UserPasswordNotFoundException;
import gift.utils.error.WishListAddFailedException;
import gift.utils.error.WishListChangeFailedException;
import gift.utils.error.WishListNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<?> handleProductExistException(ProductAlreadyExistException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotpermitNameException.class)
    public ResponseEntity<?> handleNotpermitNameExceptionException(NotpermitNameException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenAuthException.class)
    public ResponseEntity<?> handleTokenAuthException(TokenAuthException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserPasswordNotFoundException.class)
    public ResponseEntity<?> handleUserPasswordNotFoundException(UserPasswordNotFoundException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WishListAddFailedException.class)
    public ResponseEntity<?> handleWishListAddFailedException(WishListAddFailedException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WishListNotFoundException.class)
    public ResponseEntity<?> handleWishListNotFoundException(WishListNotFoundException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WishListChangeFailedException.class)
    public ResponseEntity<?> handleWishListChangeFailedException(WishListChangeFailedException ex,
        WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException ex,
        WebRequest request){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateOptionException.class)
    public ResponseEntity<?> handleDuplicateOptionException(DuplicateOptionException ex,
        WebRequest request){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OptionNameDuplicationException.class)
    public ResponseEntity<?> handleOptionNameDuplicationException(OptionNameDuplicationException ex,
        WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<?> handleOptionNotFoundException(OptionNotFoundException ex,
        WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(KakaoLoginException.class)
    public ResponseEntity<?> handleKakaoLoginException(KakaoLoginException ex,
        WebRequest request){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
