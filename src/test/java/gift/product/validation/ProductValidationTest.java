package gift.product.validation;

import gift.product.dto.ProductDTO;
import gift.product.exception.InstanceValueException;
import gift.product.exception.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductValidationTest {

    @Autowired
    private ProductValidation productValidation;

    @Test
    void testRegisterIncludeKaKao() {
        System.out.println("[ProductServiceTest] testRegisterIncludeKaKao()");
        ProductDTO productDTO = new ProductDTO("카카오프렌즈", 5000, "image.url", 1L);

        Assertions.assertThrows(InstanceValueException.class, () -> {
            productValidation.registerValidation(productDTO);
        });
    }

    @Test
    void testRegisterNegativePrice() {
        System.out.println("[ProductServiceTest] testRegisterIncludeKaKao()");
        ProductDTO productDTO = new ProductDTO("카카오프렌즈", -1, "image.url", 1L);

        Assertions.assertThrows(InstanceValueException.class, () -> {
            productValidation.registerValidation(productDTO);
        });
    }

    @Test
    void testUpdateNotExistId() {
        System.out.println("[ProductServiceTest] testUpdateNotExistId()");
        ProductDTO productDTO = new ProductDTO("product", 1000, "image", 1L);

        Assertions.assertThrows(InvalidIdException.class, () -> {
            productValidation.updateValidation(-1L, productDTO);
        });
    }

}