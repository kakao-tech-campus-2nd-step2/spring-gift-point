package gift.controller.exception;

import gift.dto.common.apiResponse.ApiResponseBody.FailureBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class OauthExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<FailureBody> handleHttpClientError(
        HttpClientErrorException e) {
        return ApiResponseGenerator.fail((HttpStatus) e.getStatusCode(),
            "메세지 전송 오류", String.valueOf(e.getStatusCode()));
    }
}
