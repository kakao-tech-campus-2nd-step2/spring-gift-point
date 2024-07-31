package gift.product.option.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Options {

    private final Set<Option> options;

    public Options(Set<Option> options) {
        this.options = new HashSet<>(options);
    }

    public void validateNameDuplicate(Option other) {
        if (options.stream()
            .map(Option::getName)
            .collect(Collectors.toSet())
            .contains(other.getName())) {
            throw new CustomException(ErrorCode.OPTION_NAME_DUPLICATE);
        }
    }

    public void addOption(Option option) {
        validateNameDuplicate(option);
        options.add(option);
    }

    public void removeOption(Option option) {
        if (!options.contains(option)) {
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }

        if (options.size() == 1 && options.contains(option)) {
            throw new CustomException(ErrorCode.LAST_OPTION);
        }

        options.remove(option);
    }

    public Set<Option> getSet() {
        return options;
    }

}
