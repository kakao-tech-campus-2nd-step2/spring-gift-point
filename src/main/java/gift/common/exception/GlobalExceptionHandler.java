package gift.common.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("유효하지 않은 입력입니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("잘못된 형태의 데이터 값을 입력하셨습니다.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ProblemDetail> invalidTokenException(JwtException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ProblemDetail> productException(ProductException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ProblemDetail> userException(UserException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(WishException.class)
    public ResponseEntity<ProblemDetail> wishException(WishException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
  
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ProblemDetail> categoryException(CategoryException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(OptionException.class)
    public ResponseEntity<ProblemDetail> optionException(OptionException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<ProblemDetail> oAuthException(OAuthException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getHttpStatus());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}
