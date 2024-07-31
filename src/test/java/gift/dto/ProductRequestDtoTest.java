package gift.dto;

import gift.core.exception.ValidationMessage;
import gift.feat.product.contoller.dto.ProductRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductRequestDtoTest {

	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("'카카오'를 포함하지 않는 정상적인 '한글' 상품명을 가진 ProductRequestDto를 생성한다.")
	public void testValidProductRequestDtoWithKoreanName() {
		ProductRequestDto product = new ProductRequestDto( "초콜릿", 1000L, "http://example.com/image.png");

		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("'카카오'를 포함하지 않는 정상적인 상품명을 가진 ProductRequestDto를 생성한다.")
	public void testValidProductRequestDto() {
		ProductRequestDto product = new ProductRequestDto("chocolate", 1000L, "http://example.com/image.png");

		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("상품명에 '카카오'를 포함하면 ProductRequestDto를 생성할 수 없다.")
	public void testProductNameContainsKakao() {
		ProductRequestDto product = new ProductRequestDto("카카오초콜릿", 1000L, "http://example.com/image.png");

		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);

		assertEquals(1, violations.size());
		// List<String> violationMessages = violations.stream()
		// 	.map(ConstraintViolation::getMessage)
		// 	.collect(Collectors.toList());
		// System.out.println(violationMessages);
		assertEquals(ValidationMessage.NO_KAKAO_MSG, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품명에 유효하지 않은 특수문자를 포함하면 ProductRequestDto를 생성할 수 없다.")
	public void testProductNameWithInvalidCharacters() {
		ProductRequestDto product = new ProductRequestDto( "Chocolate@!", 1000L, "http://example.com/image.png");

		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);
		assertEquals(ValidationMessage.INVALID_CHARSET_MSG, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품명이 15자를 초과하면 ProductRequestDto를 생성할 수 없다.")
	public void testProductNameTooLong() {
		ProductRequestDto product = new ProductRequestDto("VeryLongProductNameThatExceedsTheLimit", 1000L, "http://example.com/image.png");

		Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(product);
		assertFalse(violations.isEmpty());
		assertEquals(1, violations.size());
		assertEquals(ValidationMessage.INVALID_SIZE_MSG, violations.iterator().next().getMessage());
	}
}