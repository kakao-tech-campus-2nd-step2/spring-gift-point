package gift.validation;

import gift.dto.OptionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueOptionNamesValidator implements
    ConstraintValidator<UniqueOptionNames, List<OptionRequest>> {

    @Override
    public boolean isValid(List<OptionRequest> optionRequests, ConstraintValidatorContext context) {
        if (optionRequests == null) {
            return true;
        }

        Set<String> uniqueNames = new HashSet<>();
        for (OptionRequest option : optionRequests) {
            if (!uniqueNames.add(option.getName())) {
                return false;
            }
        }
        return true;
    }
}
