package gift.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionList {
    private final List<String> options;

    public OptionList(List<String> options) {
        this.options = List.copyOf(options);
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
