package gift.exception;

import gift.category.dto.CategoryResponse;
import gift.dto.ApiResponse;
import gift.member.dto.MemberResponse;
import gift.model.HttpResult;
import gift.option.dto.OptionResponse;
import gift.product.dto.ProductResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(
        ProductNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(
        MethodArgumentNotValidException e, Model model) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        response.put("message", "Validation failed");
        model.addAttribute("errorMessage", response.get("message"));
        return "redirect:/";
    }

    @ExceptionHandler(ForbiddenWordException.class)
    public String handleIllegalArgumentException(
        ForbiddenWordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleNotRegisterdAccount(ForbiddenException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(NonIntegerPriceException.class)
    public String handleNonIntegerPrice(NonIntegerPriceException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "addproductform";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        String message = String.format("'%s' should be a valid '%s' and '%s' isn't", name, type,
            value);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalCategoryException.class)
    public final ResponseEntity<CategoryResponse> handleNotFoundCategoryException(
        IllegalCategoryException e) {
        Map<String, Object> categoryResponse = new LinkedHashMap<>();
        categoryResponse.put("Error", "존재 하지 않는 카테고리 입니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new CategoryResponse(HttpResult.ERROR, "에러 발생",
                HttpStatus.NOT_FOUND,
                Collections.singletonList(categoryResponse)
            ));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalProductException.class)
    public final ResponseEntity<ProductResponse> handleNotFoundProductException(
        IllegalProductException e) {
        Map<String, Object> categoryResponse = new LinkedHashMap<>();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ProductResponse(HttpResult.ERROR,
                    "존재 하지 않는 상품입니다.",
                    HttpStatus.NOT_FOUND,
                    null
                ));
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalAccessWithOutLoginException.class)
    public final ResponseEntity<ApiResponse> handleIllegalAcessWithoutLoginException(
        IllegalAccessWithOutLoginException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ApiResponse(HttpResult.ERROR, "로그인되지 않았습니다.", HttpStatus.UNAUTHORIZED));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalEmailException.class)
    public final ResponseEntity<MemberResponse> handleIllegalEmailException(
        IllegalEmailException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "잘못된 이메일 형식입니다.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new MemberResponse(HttpResult.ERROR,
                "예외 발생",
                HttpStatus.FORBIDDEN,
                Collections.singletonList(response)
            )
        );
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(OptionNotFoundException.class)
    public final ResponseEntity<OptionResponse> handleOptionNotFound(OptionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new OptionResponse(HttpResult.OK, "존재 하지 않는 옵션입니다.", HttpStatus.NOT_FOUND, null)
        );
    }
}