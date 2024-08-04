package gift.model.product;

import java.util.ArrayList;
import java.util.List;

public class Options {

    private List<Option> options;

    public Options(List<Option> options) {
        if (!validateOptions(options)) {
            throw new IllegalArgumentException("중복된 option 이름이 존재합니다.");
        }
        this.options = new ArrayList<>(options);
    }

    public List<Option> getOptions() {
        return new ArrayList<>(options);
    }

    public boolean isDeletePossible() {
        return options.size() > 1;
    }

    public static boolean validateOptions(List<Option> options) {
        if (options.stream().map(Option::getName).distinct().count() != options.size()) {
            return false;
        }
        return true;
    }

    public Options merge(Options newOptions) {
        List<Option> mergedOptions = new ArrayList<>(options);
        mergedOptions.addAll(newOptions.getOptions());
        return new Options(mergedOptions);
    }

    public boolean isUpdatePossible(Option option, String name) {
        if (option.isSameName(name)) {
            return true;
        }
        if (options.stream().anyMatch(o -> o.isSameName(name))) {
            return false;
        }
        return true;
    }
}
