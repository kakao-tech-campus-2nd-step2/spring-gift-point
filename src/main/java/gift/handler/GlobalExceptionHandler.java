package gift.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleValidationException(Exception ex, Model model, WebRequest request) {
        if (isJsonRequest(request)) {
            return new ResponseEntity<>(new ErrorResponse("Validation error: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("error", "Validation error: " + ex.getMessage());
            return "error/400";
        }
    }

    private boolean isJsonRequest(WebRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }

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
