package gift.model.valueObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionNameList {
    private final List<String> options;

    public OptionNameList(List<String> options) {
        if (hasDuplicates(options)) {
            throw new IllegalArgumentException("옵션은 중복될 수 없습니다.");
        }
        this.options = options;
    }

    private boolean hasDuplicates(List<String> options) {
        Set<String> optionSet = new HashSet<>(options);
        return options.size() != optionSet.size();
    }

    @Override
    public String toString() {
        return "OptionList{" +
                "options=" + options +
                '}';
    }
}
