package gift.core.annotaions;

import gift.core.exception.ValidationMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoKakaoValidator implements ConstraintValidator<NoKakao, String> {
	@Override
	public void initialize(NoKakao constraintAnnotation) {
	}
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return !value.contains("카카오");
	}
}
