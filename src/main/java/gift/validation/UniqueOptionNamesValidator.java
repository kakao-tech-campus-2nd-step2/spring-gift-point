package gift.validation;

import gift.dto.request.OptionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueOptionNamesValidator implements ConstraintValidator<UniqueOptionNames, List<OptionRequest>> {

    @Override
    public boolean isValid(List<OptionRequest> value, ConstraintValidatorContext context) {
        Set<String> uniqueNames = new HashSet<>();

        return value.stream()
                .map(OptionRequest::name)
                .allMatch(name -> uniqueNames.add(name));
    }
}
