package gift.validation.validator;

import gift.entity.product.Product;
import gift.repository.ProductRepository;
import gift.validation.constraint.ProductIdConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class ProductIdValidator implements ConstraintValidator<ProductIdConstraint, Long> {

    private final ProductRepository productRepository;

    public ProductIdValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void initialize(ProductIdConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long productId, ConstraintValidatorContext cxt) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return returnValidationResult("Such product doesn't exist", cxt);
        }
        return true;
    }

    private static boolean returnValidationResult(String errorMsg, ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate(errorMsg)
                .addConstraintViolation();
        return false;
    }
}
