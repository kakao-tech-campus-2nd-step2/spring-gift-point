package gift.util;

import gift.dto.betweenClient.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {
    public static ResponseEntity<ResponseDTO> responseError(Exception e, HttpStatus status) {
        return new ResponseEntity<>(new ResponseDTO(true, e.getMessage()), status);
    }
}
