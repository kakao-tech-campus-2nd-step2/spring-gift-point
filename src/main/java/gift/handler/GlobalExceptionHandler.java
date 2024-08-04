package gift.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(Exception ex, Model model) {
        model.addAttribute("error", "Validation error: " + ex.getMessage());
        return "error/400";
    }

    // JSON 응답을 처리하는 예외 핸들러
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleValidationExceptionJson(Exception ex, WebRequest request) {
        if ("application/json".equals(request.getHeader("Accept"))) {
            return new ResponseEntity<>(new ErrorResponse("Validation error: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    // 예외 응답 구조 정의
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
