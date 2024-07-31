package gift.core.annotaions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CharSetValidator implements ConstraintValidator<ValidCharset, String> {
	private String pattern;

	@Override
	public void initialize(ValidCharset constraintAnnotation) {
		this.pattern = constraintAnnotation.pattern();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext context) {
		if (s == null) {
			return true;
		}
		return s.matches(pattern);
	}
}
