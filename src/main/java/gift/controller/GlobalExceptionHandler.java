package gift.controller;

import gift.utils.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleProductExistException(ProductAlreadyExistException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotpermitNameException.class)
    public ResponseEntity<ErrorResponse> handleNotpermitNameExceptionException(NotpermitNameException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(403, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenAuthException.class)
    public ResponseEntity<ErrorResponse> handleTokenAuthException(TokenAuthException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(401, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserPasswordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserPasswordNotFoundException(UserPasswordNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(401, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WishListAddFailedException.class)
    public ResponseEntity<ErrorResponse> handleWishListAddFailedException(WishListAddFailedException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WishListNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWishListNotFoundException(WishListNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WishListChangeFailedException.class)
    public ResponseEntity<ErrorResponse> handleWishListChangeFailedException(WishListChangeFailedException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateOptionException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateOptionException(DuplicateOptionException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptionNameDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleOptionNameDuplicationException(OptionNameDuplicationException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOptionNotFoundException(OptionNotFoundException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(KakaoLoginException.class)
    public ResponseEntity<ErrorResponse> handleKakaoLoginException(KakaoLoginException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex,
        WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(401, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
