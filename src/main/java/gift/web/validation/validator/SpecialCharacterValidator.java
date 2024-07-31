package gift.web.validation.validator;

import gift.utils.StringUtils;
import gift.web.validation.constraints.SpecialCharacter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SpecialCharacterValidator implements ConstraintValidator<SpecialCharacter, String> {

    private Set<Character> allowedSpecialChars;
    private final String DELIMITER = ",";

    @Override
    public void initialize(SpecialCharacter constraintAnnotation) {
        String allowedChars = constraintAnnotation.allowed();
        allowedSpecialChars = Arrays.stream(allowedChars.split(DELIMITER))
            .map(String::trim)
            .map(s -> s.charAt(0))
            .collect(Collectors.toSet());

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.containsOnlyAllowedSpecialChars(value, allowedSpecialChars);
    }
}
