package gift.member.exception;

import gift.common.util.ApiResponse;
import gift.member.application.MemberController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberErrorHandler {
    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberAlreadyExistsException(MemberAlreadyExistsException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(MemberAuthorizedErrorException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberAuthorizedErrorException(MemberAuthorizedErrorException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e.getStatus(), e.getMessage()));
    }
}
