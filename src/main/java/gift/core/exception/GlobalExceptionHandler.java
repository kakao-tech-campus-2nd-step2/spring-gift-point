package gift.core.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import gift.core.exception.product.CategoryNotFoundException;
import gift.core.exception.product.DuplicateProductIdException;
import gift.core.exception.product.ProductNotFoundException;
import gift.core.exception.user.PasswordNotMatchException;
import gift.core.exception.user.UserAlreadyExistsException;
import gift.core.exception.user.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// ProblemDetail 중, 현재 반환 가능한 값만 설정하여 반환한다.

	// product 관련 예외 처리
	@ExceptionHandler(ProductNotFoundException.class)
	public ProblemDetail handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle("Product Not Found");
		return problemDetail;
	}

	@ExceptionHandler(DuplicateProductIdException.class)
	public ProblemDetail handleDuplicateProductIdException(DuplicateProductIdException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problemDetail.setTitle("Duplicate Product ID");
		return problemDetail;
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ProblemDetail handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle("Category Not Found");
		return problemDetail;
	}

	// user 관련 예외 처리
	@ExceptionHandler(UserNotFoundException.class)
	public ProblemDetail handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle("User Not Found");
		return problemDetail;
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problemDetail.setTitle("User Already Exists");
		return problemDetail;
	}

	@ExceptionHandler(PasswordNotMatchException.class)
	public ProblemDetail handlePasswordNotMatchException(PasswordNotMatchException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
		problemDetail.setTitle("Password Not Match");
		return problemDetail;
	}

	// validation 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
		problemDetail.setTitle("Validation Error");

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		problemDetail.setProperty("errors", errors);

		return problemDetail;
	}
}