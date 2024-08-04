package gift.advice;

import gift.dto.response.MessageResponseDTO;
import gift.exception.KakaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KakaoExceptionAdvice {

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<MessageResponseDTO> handleKakaoResponseError(KakaoException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new MessageResponseDTO(e.getMessage()));
    }

}
