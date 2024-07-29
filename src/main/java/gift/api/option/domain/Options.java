package gift.api.option.domain;

import gift.api.option.exception.InvalidNameException;
import java.util.List;

public class Options {

    private List<Option> options;

    public Options(List<Option> options) {
        this.options = options;
    }

    public static Options of(List<Option> options) {
        return new Options(options);
    }

    public void validateUniqueName(Option option) {
        if (options.stream()
                .anyMatch(it -> it.getName().equals(option.getName()))) {
            throw new InvalidNameException();
        }
    }
}
