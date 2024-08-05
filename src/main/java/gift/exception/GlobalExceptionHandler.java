package gift.exception;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static List<ErrorDTO> getErrorDTOS(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<ErrorDTO> errorDetails = new ArrayList<>();

        for (ObjectError objectError : allErrors) {
            addErrorDetailIfFieldError(errorDetails, objectError);
        }

        return errorDetails;
    }

    private static void addErrorDetailIfFieldError(List<ErrorDTO> errorDetails,
        ObjectError objectError) {
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            ErrorDTO errorDTO = new ErrorDTO(fieldName, errorMessage);
            errorDetails.add(errorDTO);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
        MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = createProblemDetail(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    private ProblemDetail createProblemDetail(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("/errors/validation-failed"));
        problemDetail.setTitle("Validation Failed");
        problemDetail.setDetail("하나 이상의 Validation 문제가 있습니다.");

        BindingResult bindingResult = exception.getBindingResult();
        List<ErrorDTO> errorDetails = getErrorDTOS(bindingResult);

        problemDetail.setProperty("errors", errorDetails);
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleProductNotFoundException(
        ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/product-not-found"));
        problemDetail.setTitle("Product Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(UnauthorizedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/unauthorized-access"));
        problemDetail.setTitle("Unauthorized Access");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExistsException(
        EmailAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/email-already-exists"));
        problemDetail.setTitle("Email Already Exists");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<ProblemDetail> handleWrongAuthorizedException(
        UserAuthException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/wrong-authorized-access"));
        problemDetail.setTitle("Wrong Authorized Access");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
    }

    @ExceptionHandler(WishNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleWishNotFoundException(
        WishNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/wish-not-found"));
        problemDetail.setTitle("Wish Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCategoryNotFoundException(
        CategoryNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/category-not-found"));
        problemDetail.setTitle("Category Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(CategoryHasProductsException.class)
    public ResponseEntity<ProblemDetail> handleCategoryHasProductsException(
        CategoryHasProductsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/category-has-products"));
        problemDetail.setTitle("Category Has Products");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(OptionNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleOptionNotFoundException(
        OptionNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/option-not-found"));
        problemDetail.setTitle("Option Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(MinimumOptionException.class)
    public ResponseEntity<ProblemDetail> handleMinimumOptionException(
        MinimumOptionException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/minimum-option"));
        problemDetail.setTitle("Minimum Option");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(DuplicateOptionNameException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateOptionNameException(
        DuplicateOptionNameException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/duplicate-option-name"));
        problemDetail.setTitle("Duplicate Option Name");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(MinimumPointException.class)
    public ResponseEntity<ProblemDetail> handleMinimumPointException(
        MinimumPointException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/minimum-point"));
        problemDetail.setTitle("Minimum Point");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(KakaoNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleKakaoApiException(
        KakaoNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("/errors/kakao-not-found"));
        problemDetail.setTitle("Kakao Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ProblemDetail> handleHttpClientErrorException(
        HttpClientErrorException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(),
            ex.getResponseBodyAsString());
        problemDetail.setType(URI.create("/errors/http-client-error"));
        problemDetail.setTitle("HTTP Client Error");
        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ProblemDetail> handleHttpServerErrorException(
        HttpServerErrorException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(),
            ex.getResponseBodyAsString());
        problemDetail.setType(URI.create("/errors/http-server-error"));
        problemDetail.setTitle("HTTP Server Error");
        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
    }
}
