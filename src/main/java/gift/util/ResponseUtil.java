package gift.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResponseUtil {

  protected static <T> ResponseEntity<T> createResponse(T data, HttpStatus status) {
    return new ResponseEntity<>(data, status);
  }

  protected <T> ResponseEntity<T> successResponse(T data) {
    return createResponse(data, HttpStatus.OK);
  }

  protected ResponseEntity<String> successMessage(String message) {
    return createResponse(message, HttpStatus.OK);
  }

  protected ResponseEntity<String> errorMessage(String messageStatus) {
    return createResponse(messageStatus, HttpStatus.BAD_REQUEST);
  }

  protected <T> ResponseEntity<T> errorResponse(T data, String messageStatus) {
    return createResponse(data, HttpStatus.BAD_REQUEST);
  }
}
