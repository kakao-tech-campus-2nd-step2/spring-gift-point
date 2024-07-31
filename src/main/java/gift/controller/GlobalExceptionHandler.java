package gift.controller;

import gift.exception.KakaoValidationException;
import gift.exception.StringValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "GlobalExceptionHandler", description = "글로벌 예외 처리기")
@ControllerAdvice
public class GlobalExceptionHandler {

  @Operation(summary = "카카오 예외 처리", description = "카카오 인증 관련 예외를 처리합니다.")
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(KakaoValidationException.class)
  public String handleKakaoException(KakaoValidationException ex, Model model) {
    model.addAttribute("errorMessage", ex.getMessage());
    return "kakaoerror";
  }

  @Operation(summary = "문자열 검증 예외 처리", description = "문자열 검증 관련 예외를 처리합니다.")
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(StringValidationException.class)
  public String handleStringException(StringValidationException ex, Model model) {
    model.addAttribute("errorMessage", ex.getMessage());
    return "validation-error";
  }

  @Operation(summary = "메서드 인자 검증 예외 처리", description = "메서드 인자 검증 관련 예외를 처리합니다.")
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
    model.addAttribute("errorMessage", ex.getMessage());
    return "validation-error";
  }

  @Operation(summary = "잘못된 인자 예외 처리", description = "잘못된 인자 관련 예외를 처리합니다.")
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
    model.addAttribute("errorMessage", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }
}