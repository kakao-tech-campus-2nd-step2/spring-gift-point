package gift.domain.option;

import java.util.ArrayList;
import java.util.List;

public class Options {
    private List<Option> options;

    public Options(List<Option> options) {
        this.options = new ArrayList<>(options);
    }

    public void validate(Option option) {
        if(options.stream().anyMatch(it -> it.isSameName(option))) {
            throw new IllegalArgumentException("Option already exists");
        }

        options.add(option);
    }

    public List<Option> toList() {
        return new ArrayList<>(options);
    }
}
