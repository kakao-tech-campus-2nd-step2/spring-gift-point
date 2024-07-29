package gift.exception;

import gift.category.service.CategoryService;
import gift.product.domain.Product;
import gift.product.domain.ProductDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final CategoryService categoryService;

    @Autowired
    public GlobalExceptionHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException ex, Model model) {
        String errorMessage = ex.getMessage();
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.findAll());
        return "add_product";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException ex, Model model) {
        String errorMessage = "이 상품에 대해 같은 이름의 옵션이 존재합니다. 다른 이름을 입력해주세요";
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.findAll());
        return "add_product";
    }
}
