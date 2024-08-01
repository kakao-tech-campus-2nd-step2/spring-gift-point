package gift.exception;
import gift.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Product> handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Product> handleProductNotFoundException(ProductNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Product not found: " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Product> handleInvalidProductException(InvalidProductException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid product: " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}