//package gift.DTO;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import jakarta.validation.*;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ProductOptionDTOTest {
//
//    private Validator validator;
//
//    @BeforeEach
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    public void testValidProductOptionDTO() {
//        ProductOptionDTO productOption = new ProductOptionDTO("ValidName", 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertTrue(violations.isEmpty(), "There should be no violations");
//    }
//
//    @Test
//    public void testNullName() {
//        ProductOptionDTO productOption = new ProductOptionDTO(null, 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Name cannot be null");
//        assertEquals(1, violations.size());
//        assertEquals("이름은 null이 될 수 없습니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testEmptyName() {
//        ProductOptionDTO productOption = new ProductOptionDTO("", 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Name cannot be empty");
//        assertEquals(1, violations.size());
//        assertEquals("이름은 1자에서 15자 사이여야 합니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testLongName() {
//        ProductOptionDTO productOption = new ProductOptionDTO("ThisNameIsWayTooLongForTheValidation", 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Name should not exceed 15 characters");
//        assertEquals(1, violations.size());
//        assertEquals("이름은 1자에서 15자 사이여야 합니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testInvalidCharactersInName() {
//        ProductOptionDTO productOption = new ProductOptionDTO("InvalidName!", 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Name contains invalid characters");
//        assertEquals(1, violations.size());
//        assertEquals("이름은 문자, 숫자, 공백 및 특수 문자 ( ) [ ] + - & / _ 만 포함할 수 있습니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testForbiddenWordInName() {
//        ProductOptionDTO productOption = new ProductOptionDTO("카카오제품", 10);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Name contains forbidden word");
//        assertEquals(1, violations.size());
//        assertEquals("카카오가 들어가는 품목명은 담당 MD와 협의한 경우에만 사용 가능합니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testNullQuantity() {
//        ProductOptionDTO productOption = new ProductOptionDTO("ValidName", 0);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertTrue(violations.isEmpty(), "Quantity can be zero");
//    }
//
//    @Test
//    public void testNegativeQuantity() {
//        ProductOptionDTO productOption = new ProductOptionDTO("ValidName", -1);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Quantity cannot be negative");
//        assertEquals(1, violations.size());
//        assertEquals("수량은 0 이상이어야 합니다.", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testExcessiveQuantity() {
//        ProductOptionDTO productOption = new ProductOptionDTO("ValidName", 100_000_001);
//
//        Set<ConstraintViolation<ProductOptionDTO>> violations = validator.validate(productOption);
//
//        assertFalse(violations.isEmpty(), "Quantity cannot exceed 100 million");
//        assertEquals(1, violations.size());
//        assertEquals("수량은 1억 이하여야 합니다.", violations.iterator().next().getMessage());
//    }
//}
