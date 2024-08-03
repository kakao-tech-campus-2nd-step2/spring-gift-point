package gift.model.valueObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionNameList {
    private final List<String> options;

    public OptionNameList(List<String> options) {
        this.options = options;
    }

    public boolean hasDuplicates() {
        Set<String> optionSet = new HashSet<>(this.options);
        return this.options.size() != optionSet.size();
    }

    @Override
    public String toString() {
        return "OptionList{" +
                "options=" + options +
                '}';
    }
}
