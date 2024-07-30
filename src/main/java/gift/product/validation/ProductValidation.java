package gift.product.validation;

import static gift.product.exception.GlobalExceptionHandler.CONTAINS_PRODUCT_NAME_KAKAO;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.PRODUCT_PRICE_NOT_POSITIVE;

import gift.product.dto.ProductDTO;
import gift.product.exception.InstanceValueException;
import gift.product.exception.InvalidIdException;
import gift.product.exception.InvalidValueException;
import gift.product.repository.CategoryRepository;
import gift.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductValidation {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductValidation(
        ProductRepository productRepository,
        CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public void registerValidation(ProductDTO productDTO) {
        System.out.println("[ProductValidation] registerValidation()");
        validateIncludeNameKakao(productDTO.getName());
        validatePrice(productDTO.getPrice());
        validateExistCategoryId(productDTO.getCategoryId());
    }

    public void updateValidation(Long id, ProductDTO productDTO) {
        System.out.println("[ProductValidation] updateValidation()");
        validateExistProductId(id);
        registerValidation(productDTO);
    }

    public void deleteValidation(Long id) {
        System.out.println("[ProductValidation] deleteValidation()");
        validateExistProductId(id);
    }

    private void validateIncludeNameKakao(String name) {
        if(name.contains("카카오"))
            throw new InvalidValueException(CONTAINS_PRODUCT_NAME_KAKAO);
    }

    private void validatePrice(int price) {
        if(price <= 0)
            throw new InstanceValueException(PRODUCT_PRICE_NOT_POSITIVE);
    }

    private void validateExistCategoryId(Long categoryId) {
        if(!categoryRepository.existsById(categoryId))
            throw new InvalidIdException(NOT_EXIST_ID);
    }
    
    private void validateExistProductId(Long id) {
        System.out.println("[ProductValidation] validateExistId()");
        if(!productRepository.existsById(id))
            throw new InvalidIdException(NOT_EXIST_ID);
    }
}
