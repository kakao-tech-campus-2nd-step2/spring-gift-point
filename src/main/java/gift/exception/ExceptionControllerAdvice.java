package gift.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.response.ErrorResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private final ObjectMapper objectMapper;

    public ExceptionControllerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e) {
        ErrorCode error = ErrorCode.VALIDATION_ERROR;

        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .code(error.getStatus().value())
                .message(error.getMessage())
                .build();

        e.getFieldErrors().forEach(fieldError -> response.addValidation(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(GiftException.class)
    public ResponseEntity<ErrorResponse> giftException(GiftException e) {
        HttpStatus status = e.getErrorMessage().getStatus();

        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .code(status.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(status)
                .body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<KakaoErrorResponse> kakaoLoginException(HttpClientErrorException e) {
        HttpStatusCode statusCode = e.getStatusCode();

        String responseBody = extractResponseBody(e.getMessage());
        JsonNode jsonNode = extractJsonFromMessage(responseBody);

        String error = jsonNode.get("error").asText();
        String errorDescription = jsonNode.get("error_description").asText();
        String errorCode = jsonNode.get("error_code").asText();

        KakaoErrorResponse kakaoErrorResponse = new KakaoErrorResponse(error, errorDescription, errorCode);

        return ResponseEntity.status(statusCode)
                .body(kakaoErrorResponse);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> sortPropertyNotExists(PropertyReferenceException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .code(status.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(status)
                .body(response);
    }

    private String extractResponseBody(String message) {
        int startIndex = message.indexOf('{');
        int endIndex = message.lastIndexOf('}') + 1;
        if (startIndex == -1 || endIndex == -1) {
            throw new GiftException(ErrorCode.JSON_PARSING_FAILED);
        }
        return message.substring(startIndex, endIndex);
    }

    private JsonNode extractJsonFromMessage(String message) {
        try {
            return objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            throw new GiftException(ErrorCode.JSON_PARSING_FAILED);
        }
    }

}
