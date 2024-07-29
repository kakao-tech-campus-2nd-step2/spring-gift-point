package gift.exception;

import gift.product.ResponseDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        HashMap<String, String> map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            map.put(err.getField(), err.getDefaultMessage());
        });

        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                map
        );
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleAuthenticationException(JwtException ex){
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.UNAUTHORIZED,
                "JwtException",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleArgumentException(IllegalArgumentException ex){
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST,
                "IllegalArgumentException",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);

    }

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<?> handleKakaoException(KakaoException ex){
        ResponseDTO responseDTO = new ResponseDTO(
                ex.getErrorCode().getHttpStatus(),
                ex.getErrorCode().getCode(),
                ex.getMessage()

        );
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(responseDTO);

    }

}
