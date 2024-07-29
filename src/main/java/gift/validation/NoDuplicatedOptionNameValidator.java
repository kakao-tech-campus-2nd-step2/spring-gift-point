package gift.validation;

import gift.dto.request.OptionCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoDuplicatedOptionNameValidator implements ConstraintValidator<NoDuplicatedOptionName, List<OptionCreateRequest>> {

    @Override
    public boolean isValid(List<OptionCreateRequest> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        Set<String> optionNames = value.stream()
                .map(OptionCreateRequest::getName)
                .collect(Collectors.toSet());

        return optionNames.size() == value.size();
    }

}
