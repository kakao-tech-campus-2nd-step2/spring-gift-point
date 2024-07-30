package gift.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ContainsOnlyAllowedSpecialCharacterValidator implements ConstraintValidator<ContainsOnlyAllowedSpecialCharacter, String> {

    private static final Set<Character> ALLOWED_SPECIAL_CHARACTERS = new HashSet<>(Arrays.asList('(', ')', '[', ']', '+', '-', '&', '/', '_', ' '));

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.chars()
                .allMatch(c -> Character.isLetterOrDigit(c) || ALLOWED_SPECIAL_CHARACTERS.contains((char) c));
    }

}
