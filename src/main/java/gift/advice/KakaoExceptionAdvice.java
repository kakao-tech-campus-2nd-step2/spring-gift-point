package gift.exception.advice;

import gift.entity.MessageResponseDTO;
import gift.exception.KakaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KakaoExceptionAdvice {

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<MessageResponseDTO> handleResponseError(KakaoException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new MessageResponseDTO(e.getMessage()));
    }

}
